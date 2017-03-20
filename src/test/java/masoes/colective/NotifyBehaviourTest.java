/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import agent.AgentManagementAssistant;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import masoes.ontology.MasoesOntology;
import masoes.ontology.notifier.NotifyAction;
import masoes.ontology.notifier.NotifyEvent;
import masoes.ontology.notifier.NotifyObject;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import ontology.OntologyAssistant;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import test.PowerMockitoTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class NotifyBehaviourTest extends PowerMockitoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Agent agentMock;
    private NotifyBehaviour notifyBehaviour;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionArgumentCaptor;
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private OntologyAssistant ontologyAssistant;
    private AID actorAID;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionArgumentCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);
        agentMock = mock(Agent.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);
        ontologyAssistant = new OntologyAssistant(agentMock, MasoesOntology.getInstance());

        notifyBehaviour = new NotifyBehaviour(agentMock);
        setFieldValue(notifyBehaviour, "agentManagementAssistant", agentManagementAssistantMock);
        setFieldValue(notifyBehaviour, "ontologyAssistant", ontologyAssistant);

        actorAID = new AID("actorName", AID.ISGUID);

        doReturn(new AID("agentName", AID.ISGUID)).when(agentMock).getAID();
    }

    @Test
    public void shouldReturnValidNotifyAction() {
        Action action = new Action(new AID(), new NotifyAction());
        assertTrue(notifyBehaviour.isValidAction(action));
    }

    @Test
    public void shouldReturnValidNotifyEvent() {
        Action action = new Action(new AID(), new NotifyEvent());
        assertTrue(notifyBehaviour.isValidAction(action));
    }

    @Test
    public void shouldReturnValidNotifyObject() {
        Action action = new Action(new AID(), new NotifyObject());
        assertTrue(notifyBehaviour.isValidAction(action));
    }

    @Test
    public void shouldSendActionStimulusToAllEmotionalAgents() throws FailureException {
        testNotification(new Action(new AID(), new NotifyAction(new ActionStimulus(actorAID, "action"))));
    }

    @Test
    public void shouldSendObjectStimulusToAllEmotionalAgents() throws FailureException {
        testNotification(new Action(new AID(), new NotifyObject(new ObjectStimulus(actorAID, "object"))));
    }

    @Test
    public void shouldSendEventStimulusToAllEmotionalAgents() throws FailureException {
        testNotification(new Action(new AID(), new NotifyEvent(new EventStimulus(actorAID, "event"))));
    }

    private void testNotification(Action action) throws FailureException {
        AID agentA = actorAID;
        AID agentB = new AID("agentB", AID.ISGUID);
        AID agentC = new AID("agentC", AID.ISGUID);
        List<AID> agents = new ArrayList<>();
        agents.add(agentA);
        agents.add(agentB);
        agents.add(agentC);
        doReturn(agents).when(agentManagementAssistantMock).search(any());

        Predicate predicate = notifyBehaviour.performAction(action);
        assertThat(predicate, is(CoreMatchers.instanceOf(Done.class)));

        verify(agentManagementAssistantMock).search(serviceDescriptionArgumentCaptor.capture());
        assertThat(serviceDescriptionArgumentCaptor.getValue().getName(), is(MasoesOntology.ACTION_EVALUATE_STIMULUS));

        verify(agentMock, times(2)).send(messageArgumentCaptor.capture());

        assertThat(messageArgumentCaptor.getAllValues().get(0).getContent(), containsString("EvaluateStimulus"));
        assertThat(messageArgumentCaptor.getAllValues().get(0).getContent(), containsString("agentB"));
        assertThat(messageArgumentCaptor.getAllValues().get(1).getContent(), containsString("EvaluateStimulus"));
        assertThat(messageArgumentCaptor.getAllValues().get(1).getContent(), containsString("agentC"));
    }

}