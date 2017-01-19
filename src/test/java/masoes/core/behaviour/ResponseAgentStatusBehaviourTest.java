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
import jade.ontology.base.UnexpectedContent;
import masoes.core.Emotion;
import masoes.core.EmotionalAgent;
import masoes.core.EmotionalState;
import masoes.core.ontology.AgentStatus;
import masoes.core.ontology.EmotionStatus;
import masoes.core.ontology.GetAgentStatus;
import masoes.core.ontology.MasoesOntology;
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
public class ResponseAgentStatusBehaviourTest {

    private static final String EMOTION_NAME = "EMOTION NAME";
    private static final String BEHAVIOUR_NAME = "BEHAVIOUR NAME";
    private EmotionalAgent emotionalAgentMock;
    private ContentManager spyContentManager;
    private ACLMessage request;
    private ResponseAgentStatusBehaviour responseAgentStatusBehaviour;
    private JadeLogger mockLogger;
    private Emotion emotionMock;
    private Behaviour behaviourMock;

    @Before
    public void setUp() throws Exception {
        emotionalAgentMock = mock(EmotionalAgent.class);
        responseAgentStatusBehaviour = new ResponseAgentStatusBehaviour(emotionalAgentMock);

        spyContentManager = spy(new ContentManager());
        spyContentManager.registerLanguage(new SLCodec());
        spyContentManager.registerOntology(new MasoesOntology());
        doReturn(spyContentManager).when(emotionalAgentMock).getContentManager();

        emotionMock = mock(Emotion.class);
        doReturn(EMOTION_NAME).when(emotionMock).getEmotionName();
        doReturn(emotionMock).when(emotionalAgentMock).getCurrentEmotion();

        behaviourMock = mock(Behaviour.class);
        doReturn(BEHAVIOUR_NAME).when(behaviourMock).getBehaviourName();
        doReturn(behaviourMock).when(emotionalAgentMock).getCurrentEmotionalBehaviour();

        request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology(MasoesOntology.ONTOLOGY_NAME);

        mockLogger = mock(JadeLogger.class);
        setFieldValue(responseAgentStatusBehaviour, "logger", mockLogger);
    }

    @Test
    public void shouldReturnAgentStatus() throws Exception {
        AID aid = new AID();
        doReturn(aid).when(emotionalAgentMock).getAID();

        EmotionalState emotionalState = new EmotionalState();
        doReturn(emotionalState).when(emotionalAgentMock).getEmotionalState();

        GetAgentStatus getAgentStatus = new GetAgentStatus();
        Action action = new Action(aid, getAgentStatus);
        spyContentManager.fillContent(request, action);

        ACLMessage response = responseAgentStatusBehaviour.prepareResponse(request);

        AgentStatus agentStatus = (AgentStatus) spyContentManager.extractContent(response);

        AgentStatus expectedAgentStatus = new AgentStatus();
        expectedAgentStatus.setBehaviourName(BEHAVIOUR_NAME);
        expectedAgentStatus.setEmotionName(EMOTION_NAME);
        expectedAgentStatus.setAgent(aid);
        expectedAgentStatus.setEmotionStatus(new EmotionStatus(emotionalState.getActivation(), emotionalState.getSatisfaction()));

        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        assertThat(response.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(response.getOntology(), is(MasoesOntology.ONTOLOGY_NAME));
        assertReflectionEquals(expectedAgentStatus, agentStatus);
    }

    @Test
    public void shouldResponseFailureWhenException() throws Exception {
        String message = "message";
        OntologyException ontologyException = new OntologyException(message);

        ContentManager mockContentManager = mock(ContentManager.class);
        doThrow(ontologyException).when(mockContentManager).extractContent(any());

        doReturn(mockContentManager).when(emotionalAgentMock).getContentManager();

        ACLMessage response = responseAgentStatusBehaviour.prepareResponse(request);

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

        ACLMessage response = responseAgentStatusBehaviour.prepareResponse(request);
        UnexpectedContent expectedContent = new UnexpectedContent("Invalid agent action", request.getContent());
        UnexpectedContent content = (UnexpectedContent) spyContentManager.extractContent(response);

        assertThat(response.getPerformative(), is(ACLMessage.NOT_UNDERSTOOD));
        assertThat(response.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(response.getOntology(), is(MasoesOntology.ONTOLOGY_NAME));
        assertReflectionEquals(expectedContent, content);
    }

}