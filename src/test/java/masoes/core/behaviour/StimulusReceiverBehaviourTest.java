/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.logger.JadeLogger;
import jade.ontology.base.ActionResult;
import jade.ontology.base.UnexpectedContent;
import masoes.core.EmotionalAgent;
import masoes.core.ontology.EvaluateStimulus;
import masoes.core.ontology.MasoesOntology;
import masoes.core.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Behaviour.class, EmotionalAgent.class})
public class StimulusReceiverBehaviourTest {

    private EmotionalAgent emotionalAgentMock;
    private ContentManager spyContentManager;
    private ACLMessage request;
    private StimulusReceiverBehaviour stimulusReceiverBehaviour;
    private JadeLogger mockLogger;

    @Before
    public void setUp() throws Exception {
        emotionalAgentMock = mock(EmotionalAgent.class);
        stimulusReceiverBehaviour = new StimulusReceiverBehaviour(emotionalAgentMock);

        spyContentManager = spy(new ContentManager());
        spyContentManager.registerLanguage(new SLCodec());
        spyContentManager.registerOntology(new MasoesOntology());
        doReturn(spyContentManager).when(emotionalAgentMock).getContentManager();

        request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology(MasoesOntology.ONTOLOGY_NAME);

        mockLogger = mock(JadeLogger.class);
        setFieldValue(stimulusReceiverBehaviour, "logger", mockLogger);
    }

    @Test
    public void shouldEvaluateStimulus() throws Exception {
        AID aid = new AID();
        Stimulus stimulus = new Stimulus();

        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);
        Action action = new Action(aid, evaluateStimulus);
        spyContentManager.fillContent(request, action);

        ACLMessage response = stimulusReceiverBehaviour.prepareResponse(request);

        ActionResult actionResult = (ActionResult) spyContentManager.extractContent(response);
        ActionResult expectedActionResult = new ActionResult("Ok", evaluateStimulus);

        verify(emotionalAgentMock).evaluateStimulus(any());
        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        assertThat(response.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(response.getOntology(), is(MasoesOntology.ONTOLOGY_NAME));
        assertReflectionEquals(expectedActionResult, actionResult);
    }

    @Test
    public void shouldResponseFailureWhenException() throws Exception {
        String message = "message";
        OntologyException ontologyException = new OntologyException(message);

        ContentManager mockContentManager = mock(ContentManager.class);
        doThrow(ontologyException).when(mockContentManager).extractContent(any());

        doReturn(mockContentManager).when(emotionalAgentMock).getContentManager();

        ACLMessage response = stimulusReceiverBehaviour.prepareResponse(request);

        assertThat(response.getPerformative(), is(ACLMessage.FAILURE));
        assertThat(response.getContent(), is(message));
        verify(mockLogger).agentException(eq(emotionalAgentMock), eq(ontologyException));
    }

    @Test
    public void shouldReturnInvalidAgentAction() throws Exception {
        request.setContent("INVALID CONTENT");
        AgentAction mockAgentAction = mock(AgentAction.class);
        Action mockAction = mock(Action.class);
        doReturn(mockAgentAction).when(mockAction).getAction();
        doReturn(mockAction).when(spyContentManager).extractContent(request);

        ACLMessage response = stimulusReceiverBehaviour.prepareResponse(request);
        UnexpectedContent expectedContent = new UnexpectedContent("Invalid agent action", request.getContent());
        UnexpectedContent content = (UnexpectedContent) spyContentManager.extractContent(response);

        assertThat(response.getPerformative(), is(ACLMessage.NOT_UNDERSTOOD));
        assertThat(response.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(response.getOntology(), is(MasoesOntology.ONTOLOGY_NAME));
        assertReflectionEquals(expectedContent, content);
    }

}