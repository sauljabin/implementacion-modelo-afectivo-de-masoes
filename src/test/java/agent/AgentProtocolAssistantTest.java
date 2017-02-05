/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import protocol.ExtractOntologyContentException;
import protocol.FillOntologyContentException;
import util.StopWatch;
import util.StringGenerator;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class AgentProtocolAssistantTest {

    private static final String RANDOM_STRING = "randomString";
    @Rule
    ExpectedException expectedException = ExpectedException.none();
    private ArgumentCaptor<AgentAction> agentActionArgumentCaptor;
    private ArgumentCaptor<MessageTemplate> messageTemplateArgumentCaptor;
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private AgentProtocolAssistant agentProtocolAssistant;
    private Agent agentMock;
    private AID aidReceiverMock;
    private AID aidMock;
    private StringGenerator stringGeneratorMock;
    private ContentManager contentManagerMock;
    private ContainerID containerIdMock;
    private AgentProtocolAssistant agentProtocolAssistantSpy;
    private StopWatch stopWatchMock;

    @Before
    public void setUp() throws Exception {
        agentActionArgumentCaptor = ArgumentCaptor.forClass(AgentAction.class);
        messageTemplateArgumentCaptor = ArgumentCaptor.forClass(MessageTemplate.class);
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);

        agentMock = mock(Agent.class);
        aidMock = mock(AID.class);
        aidReceiverMock = mock(AID.class);
        stringGeneratorMock = mock(StringGenerator.class);
        contentManagerMock = mock(ContentManager.class);
        containerIdMock = mock(ContainerID.class);
        stopWatchMock = mock(StopWatch.class);

        agentProtocolAssistant = new AgentProtocolAssistant(agentMock);

        doReturn(aidMock).when(agentMock).getAID();
        doReturn(aidReceiverMock).when(agentMock).getAMS();
        doReturn(RANDOM_STRING).when(stringGeneratorMock).getString(anyInt());
        doReturn(containerIdMock).when(agentMock).here();

        setFieldValue(agentProtocolAssistant, "stringGenerator", stringGeneratorMock);
        setFieldValue(agentProtocolAssistant, "contentManager", contentManagerMock);
        setFieldValue(agentProtocolAssistant, "stopWatch", stopWatchMock);

        agentProtocolAssistantSpy = spy(agentProtocolAssistant);
    }

    @Test
    public void shouldConfigContentManager() throws Exception {
        agentProtocolAssistant = new AgentProtocolAssistant(agentMock);
        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames(), hasItemInArray(ConfigurableOntology.ONTOLOGY_NAME));
        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames(), hasItemInArray(JADEManagementOntology.NAME));
        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames(), hasItemInArray(BasicOntology.getInstance().getName()));
        assertThat(agentProtocolAssistant.getContentManager().getLanguageNames(), hasItemInArray(FIPANames.ContentLanguage.FIPA_SL));
    }

    @Test
    public void shouldCreateCorrectMessageRequest() {
        String expectedOntology = "expectedOntology";
        ACLMessage actual = agentProtocolAssistant.createRequestMessage(aidReceiverMock, expectedOntology);
        assertThat(actual.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(actual.getOntology(), is(expectedOntology));
        assertThat(actual.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(actual.getSender(), is(aidMock));
        assertThat(actual.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actual.getConversationId(), is(RANDOM_STRING));
        assertThat(actual.getReplyWith(), is(RANDOM_STRING));
        assertThat(actual.getAllReceiver().next(), is(aidReceiverMock));
    }

    @Test
    public void shouldSendRequestActionAndWaitDone() throws Exception {
        String expectedOntology = "expectedOntology";
        AgentAction agentActionMock = mock(AgentAction.class);
        doReturn(new Done()).when(agentProtocolAssistantSpy).sendActionAndWaitContent(aidReceiverMock, agentActionMock, expectedOntology);
        agentProtocolAssistantSpy.sendActionAndWaitDone(aidReceiverMock, agentActionMock, expectedOntology);
        verify(agentProtocolAssistantSpy).sendActionAndWaitContent(aidReceiverMock, agentActionMock, expectedOntology);
    }

    @Test
    public void shouldSendRequestActionAndGetContent() throws Exception {
        String expectedOntology = "expectedOntology";
        Done expectedContent = new Done();

        AgentAction agentActionMock = mock(AgentAction.class);
        ACLMessage responseMock = mock(ACLMessage.class);

        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
        doReturn(expectedContent).when(contentManagerMock).extractContent(responseMock);
        doReturn(responseMock).when(agentProtocolAssistantSpy).sendActionAndWaitMessage(aidReceiverMock, agentActionMock, expectedOntology);

        ContentElement contentElement = agentProtocolAssistantSpy.sendActionAndWaitContent(aidReceiverMock, agentActionMock, expectedOntology);
        assertThat(contentElement, is(expectedContent));

        verify(agentProtocolAssistantSpy).sendActionAndWaitMessage(aidReceiverMock, agentActionMock, expectedOntology);
        verify(contentManagerMock).extractContent(responseMock);
    }

    @Test
    public void shouldSendRequestAction() throws Exception {
        String expectedOntology = "expectedOntology";
        String expectedReplyWith = "expectedReplyWith";

        AgentAction agentActionMock = mock(AgentAction.class);
        ACLMessage requestMock = mock(ACLMessage.class);
        ACLMessage responseMock = mock(ACLMessage.class);

        doReturn(requestMock).when(agentProtocolAssistantSpy).createRequestMessage(aidReceiverMock, expectedOntology);
        doReturn(expectedReplyWith).when(requestMock).getReplyWith();
        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());

        ACLMessage contentElement = agentProtocolAssistantSpy.sendActionAndWaitMessage(aidReceiverMock, agentActionMock, expectedOntology);
        assertThat(contentElement, is(responseMock));

        verify(agentProtocolAssistantSpy).createRequestMessage(aidReceiverMock, expectedOntology);
        verify(contentManagerMock).fillContent(eq(requestMock), agentActionArgumentCaptor.capture());
        verify(agentMock).send(requestMock);

        AgentAction agentAction = agentActionArgumentCaptor.getValue();
        assertThat(agentAction, is(instanceOf(Action.class)));

        Action action = (Action) agentAction;
        assertThat(action.getAction(), is(agentActionMock));
        assertThat(action.getActor(), is(aidReceiverMock));

        MessageTemplate expectedMessageTemplate = MessageTemplate.MatchInReplyTo(expectedReplyWith);
        verify(agentMock).blockingReceive(messageTemplateArgumentCaptor.capture(), anyLong());
        assertReflectionEquals(expectedMessageTemplate, messageTemplateArgumentCaptor.getValue());
    }

    @Test
    public void shouldVerifyTimeoutControl() throws Exception {
        long timeout = 10000L;
        long delayInAgree = 2000L;
        ACLMessage responseMock = mock(ACLMessage.class);

        doReturn(ACLMessage.AGREE).doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        doReturn(new Done()).when(contentManagerMock).extractContent(responseMock);
        doReturn(delayInAgree).when(stopWatchMock).getTime();

        agentProtocolAssistantSpy.setTimeout(timeout);
        agentProtocolAssistantSpy.sendActionAndWaitMessage(aidReceiverMock, mock(AgentAction.class), "expectedOntology");

        InOrder inOrder = inOrder(stopWatchMock, agentMock);
        inOrder.verify(stopWatchMock).reset();
        inOrder.verify(stopWatchMock).start();
        inOrder.verify(agentMock).blockingReceive(any(MessageTemplate.class), eq(timeout));
        inOrder.verify(stopWatchMock).stop();
        inOrder.verify(stopWatchMock).getTime();
        inOrder.verify(agentMock).blockingReceive(any(MessageTemplate.class), eq(timeout - delayInAgree));
    }

    @Test
    public void shouldRegisterOntologyIsNotExist() {
        Ontology ontologyMock = mock(Ontology.class);
        agentProtocolAssistantSpy.registerOntology(ontologyMock);
        verify(contentManagerMock).registerOntology(ontologyMock);
    }

    @Test
    public void shouldWaitForMessageAfterAgreeInSendActionAndWaitMessage() throws Exception {
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.AGREE)
                .doReturn(ACLMessage.INFORM)
                .when(responseMock)
                .getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        doReturn(new Done()).when(contentManagerMock).extractContent(responseMock);
        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), any(AgentAction.class), anyString());
        verify(agentMock, times(2)).blockingReceive(any(MessageTemplate.class), anyLong());
    }

    @Test
    public void shouldThrowFillOntologyContentExceptionInSendRequestActionWhenErrorInFillContent() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
        expectedException.expect(FillOntologyContentException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), any(AgentAction.class), anyString());
    }

    @Test
    public void shouldThrowTimeoutExceptionInSendRequestActionWhenResponseIsNull() throws Exception {
        String expectedMessage = "The agent did not respond";
        doReturn(null).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expect(TimeoutException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), any(AgentAction.class), anyString());
    }

    @Test
    public void shouldThrowTimeoutExceptionInSendRequestActionWhenWaitInformAndResponseIsNull() throws Exception {
        String expectedMessage = "The agent did not respond";
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.AGREE).when(responseMock).getPerformative();
        doReturn(responseMock)
                .doReturn(null)
                .when(agentMock)
                .blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expect(TimeoutException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), any(AgentAction.class), anyString());
    }

    @Test
    public void shouldThrowInvalidResponseExceptionInSendRequestActionWhenResponseIsNotInform() throws Exception {
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("No inform response: " + ACLMessage.getPerformative(ACLMessage.NOT_UNDERSTOOD));
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.NOT_UNDERSTOOD).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentProtocolAssistantSpy.sendActionAndWaitContent(any(AID.class), any(AgentAction.class), anyString());
    }

    @Test
    public void shouldThrowExtractOntologyContentExceptionInSendRequestActionWhenErrorInExtractManager() throws Exception {
        ACLMessage responseMock = mock(ACLMessage.class);
        String expectedMessage = "expectedMessage";
        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).extractContent(any(ACLMessage.class));
        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expect(ExtractOntologyContentException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistantSpy.sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
    }

    @Test
    public void shouldThrowInvalidResponseExceptionInSendRequestActionWhenContentIsNotDone() throws Exception {
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
        String expectedContent = "content";
        doReturn(expectedContent).when(responseMock).getContent();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        doReturn(mock(ContentElement.class)).when(contentManagerMock).extractContent(responseMock);
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("Unknown notification: " + expectedContent);
        agentProtocolAssistantSpy.sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
    }

    @Test
    public void shouldSendKillAgentMessage() {
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        AID aidToKillMock = mock(AID.class);
        agentProtocolAssistantSpy.killAgent(aidToKillMock);
        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidReceiverMock), agentActionArgumentCaptor.capture(), eq(JADEManagementOntology.NAME));
        ContentElement contentElement = agentActionArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(KillAgent.class)));
        KillAgent killAgent = (KillAgent) contentElement;
        assertThat(killAgent.getAgent(), is(aidToKillMock));
    }

    @Test
    public void shouldSendCreateAgentMessage() {
        AID newAgentMock = mock(AID.class);

        String expectedAgentName = "expectedAgentName";
        Class<Agent> expectedAgentClass = Agent.class;
        String arg1 = "arg1";

        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        doReturn(newAgentMock).when(agentMock).getAID(expectedAgentName);

        AID newAgent = agentProtocolAssistantSpy.createAgent(expectedAgentName, expectedAgentClass, Arrays.asList(arg1));
        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidReceiverMock), agentActionArgumentCaptor.capture(), eq(JADEManagementOntology.NAME));

        ContentElement contentElement = agentActionArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(CreateAgent.class)));

        CreateAgent createAgent = (CreateAgent) contentElement;
        assertThat(createAgent.getAgentName(), is(expectedAgentName));
        assertThat(createAgent.getClassName(), is(Agent.class.getCanonicalName()));
        assertThat(createAgent.getContainer(), is(containerIdMock));
        assertThat(createAgent.getAllArguments().next(), is(arg1));
        assertThat(newAgent, is(newAgentMock));
    }

    @Test
    public void shouldSendCreateAgentWithNullArguments() {
        String expectedAgentName = "expectedAgentName";
        Class<Agent> expectedAgentClass = Agent.class;

        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());

        agentProtocolAssistantSpy.createAgent(expectedAgentName, expectedAgentClass, null);
        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidReceiverMock), agentActionArgumentCaptor.capture(), eq(JADEManagementOntology.NAME));

        ContentElement contentElement = agentActionArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(CreateAgent.class)));

        CreateAgent createAgent = (CreateAgent) contentElement;
        assertFalse(createAgent.getAllArguments().hasNext());
    }

    @Test
    public void shouldSendCreateAgentWithoutArguments() {
        String expectedAgentName = "expectedAgentName";
        Class<Agent> expectedAgentClass = Agent.class;
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        agentProtocolAssistantSpy.createAgent(expectedAgentName, expectedAgentClass);
        verify(agentProtocolAssistantSpy).createAgent(expectedAgentName, expectedAgentClass, null);
    }

    @Test
    public void shouldCreateAgentWithRandomNameAndWithoutArguments() {
        Class<Agent> expectedAgentClass = Agent.class;
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        agentProtocolAssistantSpy.createAgent(expectedAgentClass);
        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, expectedAgentClass, null);
    }

    @Test
    public void shouldCreateAgentWithRandomName() {
        Class<Agent> expectedAgentClass = Agent.class;
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        List argumentsMock = mock(List.class);
        agentProtocolAssistantSpy.createAgent(expectedAgentClass, argumentsMock);
        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, expectedAgentClass, argumentsMock);
    }

    @Test
    public void shouldSendCreateConfigurableAgentWithName() {
        String expectedAgentName = "expectedAgentName";
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        agentProtocolAssistantSpy.createAgent(expectedAgentName);
        verify(agentProtocolAssistantSpy).createAgent(expectedAgentName, ConfigurableAgent.class, null);
    }

    @Test
    public void shouldSendCreateConfigurableAgent() {
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        agentProtocolAssistantSpy.createAgent();
        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, ConfigurableAgent.class, null);
    }

    @Test
    public void shouldSendCreateConfigurableAgentWithArguments() {
        List argumentsMock = mock(List.class);
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        agentProtocolAssistantSpy.createAgent(argumentsMock);
        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, ConfigurableAgent.class, argumentsMock);
    }

    @Test
    public void shouldSendCreateConfigurableAgentWithArgumentsAndName() {
        List argumentsMock = mock(List.class);
        String expectedAgentName = "expectedAgentName";
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        agentProtocolAssistantSpy.createAgent(expectedAgentName, argumentsMock);
        verify(agentProtocolAssistantSpy).createAgent(expectedAgentName, ConfigurableAgent.class, argumentsMock);
    }

    @Test
    public void shouldAddBehaviour() {
        AID aidConfigurableAgentMock = mock(AID.class);
        String expectedBehaviourName = "expectedBehaviourName";

        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());

        String behaviourName = agentProtocolAssistantSpy.addBehaviour(aidConfigurableAgentMock, expectedBehaviourName, Behaviour.class);

        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidConfigurableAgentMock), agentActionArgumentCaptor.capture(), eq(ConfigurableOntology.ONTOLOGY_NAME));

        ContentElement contentElement = agentActionArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(AddBehaviour.class)));

        AddBehaviour addBehaviour = (AddBehaviour) contentElement;
        assertThat(addBehaviour.getClassName(), is(Behaviour.class.getCanonicalName()));
        assertThat(addBehaviour.getName(), is(expectedBehaviourName));
        assertThat(behaviourName, is(expectedBehaviourName));
    }

    @Test
    public void shouldAddBehaviourWithoutName() {
        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());
        AID aidMock = mock(AID.class);
        Class<Behaviour> expectedClass = Behaviour.class;
        String behaviourName = agentProtocolAssistantSpy.addBehaviour(aidMock, expectedClass);
        verify(agentProtocolAssistantSpy).addBehaviour(aidMock, RANDOM_STRING, expectedClass);
        assertThat(behaviourName, is(RANDOM_STRING));
    }

    @Test
    public void shouldRemoveBehaviour() {
        AID aidConfigurableAgentMock = mock(AID.class);
        String expectedBehaviourName = "expectedBehaviourName";

        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), any(AgentAction.class), anyString());

        agentProtocolAssistantSpy.removeBehaviour(aidConfigurableAgentMock, expectedBehaviourName);

        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidConfigurableAgentMock), agentActionArgumentCaptor.capture(), eq(ConfigurableOntology.ONTOLOGY_NAME));

        ContentElement contentElement = agentActionArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(RemoveBehaviour.class)));

        RemoveBehaviour removeBehaviour = (RemoveBehaviour) contentElement;
        assertThat(removeBehaviour.getName(), is(expectedBehaviourName));
    }


    @Test
    public void shouldInvokeKillContainerFromAgent() throws Exception {
        agentProtocolAssistant.killContainer();
        verify(contentManagerMock).fillContent(messageArgumentCaptor.capture(), agentActionArgumentCaptor.capture());
        verify(agentMock).send(messageArgumentCaptor.getValue());

        KillContainer content = new KillContainer();
        content.setContainer(containerIdMock);
        Action expectedAction = new Action(aidReceiverMock, content);

        assertReflectionEquals(expectedAction, agentActionArgumentCaptor.getValue());
    }

    @Test
    public void shouldThrowFillOntologyContentExceptionInKillContainerWhenErrorInFillContent() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
        expectedException.expect(FillOntologyContentException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistant.killContainer();
    }

    @Test
    public void shouldInvokeShutDownFromAgent() throws Exception {
        agentProtocolAssistant.shutDownPlatform();
        verify(contentManagerMock).fillContent(messageArgumentCaptor.capture(), agentActionArgumentCaptor.capture());
        verify(agentMock).send(messageArgumentCaptor.getValue());

        ShutdownPlatform content = new ShutdownPlatform();
        Action expectedAction = new Action(aidReceiverMock, content);

        assertReflectionEquals(expectedAction, agentActionArgumentCaptor.getValue());
    }

    @Test
    public void shouldThrowFillOntologyContentExceptionInShutDownWhenErrorInFillContent() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
        expectedException.expect(FillOntologyContentException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistant.shutDownPlatform();
    }

}