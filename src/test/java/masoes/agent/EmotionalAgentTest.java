/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import agent.AgentManagementAssistant;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import knowledge.Knowledge;
import language.SemanticLanguage;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.MasoesOntology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import test.PowerMockitoTest;

import java.nio.file.Paths;
import java.util.Random;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static test.ReflectionTestUtils.setFieldValue;

public class EmotionalAgentTest extends PowerMockitoTest {

    private static final String THEORY = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";

    private static final String LOCAL_NAME = "localName";
    private EmotionalAgent emotionalAgentSpy;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;
    private EmotionalAgent emotionalAgent;
    private EmotionalAgentLogger loggerMock;

    private ArgumentCaptor<Knowledge> knowledgeArgumentCaptor;
    private ArgumentCaptor<EmotionalState> emotionalStateArgumentCaptor;

    private BehaviouralComponent behaviouralComponentMock;
    private EmotionalState emotionalStateMock;

    @Before
    public void setUp() throws Exception {
        knowledgeArgumentCaptor = ArgumentCaptor.forClass(Knowledge.class);
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        emotionalStateArgumentCaptor = ArgumentCaptor.forClass(EmotionalState.class);

        behaviouralComponentMock = mock(BehaviouralComponent.class);
        loggerMock = mock(EmotionalAgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);
        emotionalAgent = createAgent();

        setFieldValue(emotionalAgent, "logger", loggerMock);
        setFieldValue(emotionalAgent, "agentManagementAssistant", agentManagementAssistantMock);

        emotionalAgentSpy = spy(emotionalAgent);
        doReturn(LOCAL_NAME).when(emotionalAgentSpy).getLocalName();

        doReturn(behaviouralComponentMock).when(emotionalAgentSpy).getBehaviouralComponent();
        emotionalStateMock = mock(EmotionalState.class);
        doReturn(emotionalStateMock).when(behaviouralComponentMock).getCurrentEmotionalState();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        emotionalAgentSpy.setup();
        InOrder inOrder = inOrder(emotionalAgentSpy);
        inOrder.verify(emotionalAgentSpy).addBehaviour(isA(EvaluateStimulusBehaviour.class));
        inOrder.verify(emotionalAgentSpy).addBehaviour(isA(ResponseAgentStateBehaviour.class));
        inOrder.verify(emotionalAgentSpy).setUp();
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class));
        try {
            emotionalAgentSpy.setup();
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(expectedException);
        }
    }

    @Test
    public void shouldCreateComponent() {
        emotionalAgentSpy.setup();
        assertThat(emotionalAgentSpy.getBehaviouralComponent(), is(notNullValue()));
    }

    @Test
    public void shouldRegisterAgent() {
        emotionalAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription evaluate = serviceDescriptionCaptor.getAllValues().get(0);
        testService(evaluate, MasoesOntology.ACTION_EVALUATE_STIMULUS);

        ServiceDescription getState = serviceDescriptionCaptor.getAllValues().get(1);
        testService(getState, MasoesOntology.ACTION_GET_EMOTIONAL_STATE);
    }

    @Test
    public void shouldSetEmotionWhenReceiveArguments() {
        Random random = new Random();

        double activation = random.nextDouble();
        double satisfaction = random.nextDouble();

        String activationString = String.valueOf(activation);
        String satisfactionString = String.valueOf(satisfaction);

        Object[] args = {"--activation=" + activationString, "--satisfaction=" + satisfactionString};

        emotionalAgentSpy.setArguments(args);
        emotionalAgentSpy.setup();

        verify(behaviouralComponentMock).setEmotionalState(emotionalStateArgumentCaptor.capture());

        assertThat(emotionalStateArgumentCaptor.getValue().getActivation(), is(activation));
        assertThat(emotionalStateArgumentCaptor.getValue().getSatisfaction(), is(satisfaction));
    }

    @Test
    public void shouldSetKnowledgePathWhenReceivePath() {
        Object[] args = {"-kp", THEORY};
        emotionalAgentSpy.setArguments(args);
        emotionalAgentSpy.setup();

        verify(behaviouralComponentMock).addKnowledge(knowledgeArgumentCaptor.capture());

        Knowledge knowledge = knowledgeArgumentCaptor.getValue();
        assertThat(knowledge.toString(), is(new Knowledge(Paths.get(THEORY)).toString().trim()));
    }

    @Test
    public void shouldSetKnowledgePathWhenReceivePathAndLongOption() {
        Object[] args = {"--knowledge-path=" + THEORY};
        emotionalAgentSpy.setArguments(args);
        emotionalAgentSpy.setup();

        verify(behaviouralComponentMock).addKnowledge(knowledgeArgumentCaptor.capture());

        Knowledge knowledge = knowledgeArgumentCaptor.getValue();
        assertThat(knowledge.toString(), is(new Knowledge(Paths.get(THEORY)).toString().trim()));
    }

    @Test
    public void shouldSetKnowledge() {
        String expectedRule = "stimulus(AGENT, eventName, 0.1, 0.1) :- self(AGENT).";
        Object[] args = {"-k", expectedRule};
        emotionalAgentSpy.setArguments(args);
        emotionalAgentSpy.setup();

        verify(behaviouralComponentMock).addKnowledge(knowledgeArgumentCaptor.capture());

        Knowledge knowledge = knowledgeArgumentCaptor.getValue();
        assertThat(knowledge.toString(), is(new Knowledge(expectedRule).toString().trim()));
    }

    @Test
    public void shouldNotSetEmotionalStateWhenArgumentsAreNull() {
        emotionalAgentSpy.setup();
        verify(behaviouralComponentMock, never()).setEmotionalState(any(EmotionalState.class));
    }

    private void testService(ServiceDescription description, String name) {
        assertThat(description.getName(), is(name));
        assertThat(description.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(description.getAllOntologies().next(), is(MasoesOntology.NAME));
        assertThat(description.getAllLanguages().next(), is(SemanticLanguage.NAME));
    }

    private EmotionalAgent createAgent() {
        return new EmotionalAgent() {

            @Override
            public void setUp() {
            }
        };
    }

}