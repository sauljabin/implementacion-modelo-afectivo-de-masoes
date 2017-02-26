/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import agent.AgentManagementAssistant;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
import ontology.OntologyAssistant;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.MasoesOntology;
import ontology.masoes.NotifyAction;
import ontology.masoes.Stimulus;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import protocol.ProtocolAssistant;
import util.MessageBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class NotifierAgentBehaviourTest {

    private static final String ACTION_NAME = "actionName";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Agent agentMock;
    private NotifierAgentBehaviour notifierAgentBehaviour;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionArgumentCaptor;
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private Action action;
    private OntologyAssistant ontologyAssistantMock;
    private ProtocolAssistant protocolAssistantMock;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionArgumentCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);
        agentMock = mock(Agent.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);
        ontologyAssistantMock = new OntologyAssistant(agentMock, MasoesOntology.getInstance());
        protocolAssistantMock = mock(ProtocolAssistant.class);

        notifierAgentBehaviour = new NotifierAgentBehaviour(agentMock);
        setFieldValue(notifierAgentBehaviour, "agentManagementAssistant", agentManagementAssistantMock);
        setFieldValue(notifierAgentBehaviour, "ontologyAssistant", ontologyAssistantMock);
        setFieldValue(notifierAgentBehaviour, "protocolAssistant", protocolAssistantMock);

        NotifyAction notifyAction = new NotifyAction();
        notifyAction.setActionName(ACTION_NAME);
        action = new Action(new AID(), notifyAction);

        doReturn(new AID("agentName", AID.ISGUID)).when(agentMock).getAID();
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action action = new Action(new AID(), new NotifyAction());
        assertTrue(notifierAgentBehaviour.isValidAction(action));
    }

    @Test
    public void shouldSendStimulusToAllEmotionalAgents() throws FailureException {
        AID agentA = new AID("agentA", AID.ISGUID);
        AID agentB = new AID("agentB", AID.ISGUID);
        List<AID> agents = Arrays.asList(
                agentA,
                agentB
        );
        doReturn(agents).when(agentManagementAssistantMock).search(any());

        Action actionDone = new Action(agentA, new EvaluateStimulus(new Stimulus(agentA, "actionName")));
        ACLMessage message = new MessageBuilder()
                .content(new Done(actionDone))
                .ontology(MasoesOntology.getInstance())
                .language(SemanticLanguage.getInstance())
                .build();

        doReturn(message).when(protocolAssistantMock).sendRequest(any(), anyInt());

        Predicate predicate = notifierAgentBehaviour.performAction(action);
        assertThat(predicate, is(CoreMatchers.instanceOf(Done.class)));

        verify(agentManagementAssistantMock).search(serviceDescriptionArgumentCaptor.capture());
        assertThat(serviceDescriptionArgumentCaptor.getValue().getName(), is(MasoesOntology.ACTION_EVALUATE_STIMULUS));

        verify(protocolAssistantMock, times(2)).sendRequest(messageArgumentCaptor.capture(), anyInt());
        assertThat(messageArgumentCaptor.getAllValues().get(0).getContent(), containsString("EvaluateStimulus"));
        assertThat(messageArgumentCaptor.getAllValues().get(0).getContent(), containsString("agentA"));
        assertThat(messageArgumentCaptor.getAllValues().get(1).getContent(), containsString("EvaluateStimulus"));
        assertThat(messageArgumentCaptor.getAllValues().get(1).getContent(), containsString("agentB"));
    }

    @Test
    public void shouldThrowCorrectException() throws Exception {
        String expectedMessage = "error";
        expectedException.expectMessage(expectedMessage);
        expectedException.expect(FailureException.class);
        doThrow(new RuntimeException(expectedMessage)).when(agentManagementAssistantMock).search(any());
        notifierAgentBehaviour.performAction(action);
    }

}