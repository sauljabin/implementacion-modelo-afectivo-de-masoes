/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.TrueProposition;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import language.SemanticLanguage;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import protocol.InvalidResponseException;
import util.MessageBuilder;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class AgentManagementAssistantTest {

    private static final String AMS_NAME = "amsName";
    private static final String AGENT_NAME = "agentName";
    private static final String CONTAINER_NAME = "containerName";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private AID amsAID = new AID(AMS_NAME, AID.ISGUID);
    private AID agentAID = new AID(AGENT_NAME, AID.ISGUID);
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private AgentManagementAssistant agentManagementAssistant;
    private Agent agentMock;
    private ACLMessage response;

    @Before
    public void setUp() {
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);

        agentMock = mock(Agent.class);
        doReturn(agentAID).when(agentMock).getAID();
        doReturn(amsAID).when(agentMock).getAMS();
        doReturn(new ContainerID(CONTAINER_NAME, null)).when(agentMock).here();

        agentManagementAssistant = new AgentManagementAssistant(agentMock);

        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(JADEManagementOntology.getInstance())
                .content(new Done(new Action(agentAID, new ShutdownPlatform())))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
    }

    @Test
    public void shouldSendShutdownToAMSAgent() throws Exception {
        agentManagementAssistant.shutdownPlatform();
        ContentElement contentElement = testSendBasicMessageToAMSAgent(JADEManagementOntology.getInstance(), false);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(ShutdownPlatform.class)));
    }

    @Test
    public void shouldSendKillContainerToAMSAgent() throws Exception {
        agentManagementAssistant.killContainer();
        ContentElement contentElement = testSendBasicMessageToAMSAgent(JADEManagementOntology.getInstance(), false);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(KillContainer.class)));
        KillContainer killContainer = (KillContainer) action.getAction();
        assertThat(killContainer.getContainer().getName(), is(CONTAINER_NAME));
    }

    @Test
    public void shouldSendKillAgentToAMSAgent() throws Exception {
        agentManagementAssistant.killAgent(agentAID);
        ContentElement contentElement = testSendBasicMessageToAMSAgent(JADEManagementOntology.getInstance(), true);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(KillAgent.class)));
        KillAgent killAgent = (KillAgent) action.getAction();
        assertThat(killAgent.getAgent().getName(), is(AGENT_NAME));
    }

    @Test
    public void shouldSendCreateAgentToAMSAgent() throws Exception {
        String arg1 = "arg1";
        agentManagementAssistant.createAgent(AGENT_NAME, Agent.class, Arrays.asList(arg1));
        ContentElement contentElement = testSendBasicMessageToAMSAgent(JADEManagementOntology.getInstance(), true);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(CreateAgent.class)));
        CreateAgent createAgent = (CreateAgent) action.getAction();
        assertThat(createAgent.getAgentName(), is(AGENT_NAME));
        assertThat(createAgent.getClassName(), is(Agent.class.getCanonicalName()));
        assertThat(createAgent.getAllArguments().next(), is(arg1));
    }

    @Test
    public void shouldSendCreateAgentToAMSAgentWithRandomName() throws Exception {
        String arg1 = "arg1";
        agentManagementAssistant.createAgent(Agent.class, Arrays.asList(arg1));
        ContentElement contentElement = testSendBasicMessageToAMSAgent(JADEManagementOntology.getInstance(), true);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(CreateAgent.class)));
        CreateAgent createAgent = (CreateAgent) action.getAction();
        assertThat(createAgent.getAgentName().length(), is(30));
        assertThat(createAgent.getClassName(), is(Agent.class.getCanonicalName()));
        assertThat(createAgent.getAllArguments().next(), is(arg1));
    }

    @Test
    public void shouldSendRemoveBehaviourToAgent() throws Exception {
        String behaviourName = "behaviourName";
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ConfigurableOntology.getInstance())
                .content(new Done(new Action(amsAID, new RemoveBehaviour(behaviourName))))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentManagementAssistant.removeBehaviour(amsAID, behaviourName);
        ContentElement contentElement = testSendBasicMessageToAMSAgent(ConfigurableOntology.getInstance(), true);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(RemoveBehaviour.class)));

        RemoveBehaviour removeBehaviour = (RemoveBehaviour) action.getAction();
        assertThat(removeBehaviour.getName(), is(behaviourName));
    }

    @Test
    public void shouldSendAddBehaviourToAgent() throws Exception {
        String behaviourName = "behaviourName";
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ConfigurableOntology.getInstance())
                .content(new Done(new Action(amsAID, new AddBehaviour(behaviourName, SimpleBehaviour.class.getCanonicalName()))))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentManagementAssistant.addBehaviour(amsAID, behaviourName, SimpleBehaviour.class);
        ContentElement contentElement = testSendBasicMessageToAMSAgent(ConfigurableOntology.getInstance(), true);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(AddBehaviour.class)));

        AddBehaviour addBehaviour = (AddBehaviour) action.getAction();
        assertThat(addBehaviour.getName(), is(behaviourName));
        assertThat(addBehaviour.getClassName(), is(SimpleBehaviour.class.getCanonicalName()));
    }

    @Test
    public void shouldSendAddBehaviourToAgentWithoutName() throws Exception {
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ConfigurableOntology.getInstance())
                .content(new Done(new Action(amsAID, new AddBehaviour("", SimpleBehaviour.class.getCanonicalName()))))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentManagementAssistant.addBehaviour(amsAID, SimpleBehaviour.class);
        ContentElement contentElement = testSendBasicMessageToAMSAgent(ConfigurableOntology.getInstance(), true);
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(AddBehaviour.class)));

        AddBehaviour addBehaviour = (AddBehaviour) action.getAction();
        assertThat(addBehaviour.getName().length(), is(30));
        assertThat(addBehaviour.getClassName(), is(SimpleBehaviour.class.getCanonicalName()));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInCreateAgent() throws Exception {
        prepareTestException(JADEManagementOntology.getInstance());
        agentManagementAssistant.createAgent(Agent.class, Arrays.asList("arg1"));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInCreateAgentWithName() throws Exception {
        prepareTestException(JADEManagementOntology.getInstance());
        agentManagementAssistant.createAgent("name", Agent.class, Arrays.asList("arg1"));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInKillAgent() throws Exception {
        prepareTestException(JADEManagementOntology.getInstance());
        agentManagementAssistant.killAgent(agentAID);
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInAddBehavior() throws Exception {
        prepareTestException(ConfigurableOntology.getInstance());
        agentManagementAssistant.addBehaviour(amsAID, SimpleBehaviour.class);
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInAddBehaviorWithName() throws Exception {
        prepareTestException(ConfigurableOntology.getInstance());
        agentManagementAssistant.addBehaviour(amsAID, "name", SimpleBehaviour.class);
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInRemoveBehaviour() throws Exception {
        prepareTestException(ConfigurableOntology.getInstance());
        agentManagementAssistant.removeBehaviour(amsAID, "");
    }

    private ContentElement testSendBasicMessageToAMSAgent(Ontology ontology, boolean verifyReceive) throws Exception {
        verify(agentMock).send(messageArgumentCaptor.capture());

        if (verifyReceive) {
            verify(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        }

        ACLMessage message = messageArgumentCaptor.getValue();

        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(message.getOntology(), is(ontology.getName()));
        assertThat(message.getLanguage(), is(SemanticLanguage.getInstance().getName()));
        assertThat(message.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(((AID) message.getAllReceiver().next()).getName(), is(AMS_NAME));
        assertThat(message.getSender().getName(), is(AGENT_NAME));

        ContentManager contentManager = new ContentManager();
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        contentManager.registerOntology(ontology);

        ContentElement contentElement = contentManager.extractContent(message);
        assertThat(contentElement, is(instanceOf(Action.class)));
        return contentElement;
    }

    private void prepareTestException(Ontology ontology) {
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ontology)
                .content(new TrueProposition())
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expectMessage(containsString("Unknown notification: "));
        expectedException.expect(InvalidResponseException.class);
    }

}