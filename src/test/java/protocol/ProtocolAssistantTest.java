/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.core.Agent;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@Ignore
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class ProtocolAssistantTest {

//    private static final String RANDOM_STRING = "randomString";
//    @Rule
//    ExpectedException expectedException = ExpectedException.none();
//    private ArgumentCaptor<AgentAction> agentActionArgumentCaptor;
//    private ArgumentCaptor<MessageTemplate> messageTemplateArgumentCaptor;
//    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
//    private ProtocolAssistant agentProtocolAssistant;
//    private Agent agentMock;
//    private AID aidReceiverMock;
//    private AID aidMock;
//    private StringGenerator stringGeneratorMock;
//    private ContentManager contentManagerMock;
//    private ContainerID containerIdMock;
//    private ProtocolAssistant agentProtocolAssistantSpy;
//    private StopWatch stopWatchMock;
//
//    @Before
//    public void setUp() throws Exception {
//        agentActionArgumentCaptor = ArgumentCaptor.forClass(AgentAction.class);
//        messageTemplateArgumentCaptor = ArgumentCaptor.forClass(MessageTemplate.class);
//        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);
//
//        agentMock = mock(Agent.class);
//        aidMock = mock(AID.class);
//        aidReceiverMock = mock(AID.class);
//        stringGeneratorMock = mock(StringGenerator.class);
//        contentManagerMock = mock(ContentManager.class);
//        containerIdMock = mock(ContainerID.class);
//        stopWatchMock = mock(StopWatch.class);
//
//        agentProtocolAssistant = new ProtocolAssistant(agentMock);
//
//        doReturn(aidMock).when(agentMock).getAID();
//        doReturn(aidReceiverMock).when(agentMock).getAMS();
//        doReturn(RANDOM_STRING).when(stringGeneratorMock).getString(anyInt());
//        doReturn(containerIdMock).when(agentMock).here();
//
//        setFieldValue(agentProtocolAssistant, "stringGenerator", stringGeneratorMock);
//        setFieldValue(agentProtocolAssistant, "contentManager", contentManagerMock);
//        setFieldValue(agentProtocolAssistant, "stopWatch", stopWatchMock);
//
//        agentProtocolAssistantSpy = spy(agentProtocolAssistant);
//    }
//
//    @Test
//    public void shouldConfigContentManager() throws Exception {
//        agentProtocolAssistant = new ProtocolAssistant(agentMock);
//        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames(), hasItemInArray(ConfigurableOntology.ONTOLOGY_NAME));
//        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames(), hasItemInArray(JADEManagementOntology.NAME));
//        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames(), hasItemInArray(BasicOntology.getInstance().getName()));
//        assertThat(agentProtocolAssistant.getContentManager().getLanguageNames(), hasItemInArray(FIPANames.ContentLanguage.FIPA_SL));
//    }
//
//    @Test
//    public void shouldCreateCorrectMessageRequest() {
//        String expectedOntology = "expectedOntology";
//        ACLMessage actual = agentProtocolAssistant.createRequestAction(aidReceiverMock, expectedOntology);
//        assertThat(actual.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
//        assertThat(actual.getOntology(), is(expectedOntology));
//        assertThat(actual.getPerformative(), is(ACLMessage.REQUEST));
//        assertThat(actual.getSender(), is(aidMock));
//        assertThat(actual.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
//        assertThat(actual.getConversationId(), is(RANDOM_STRING));
//        assertThat(actual.getReplyWith(), is(RANDOM_STRING));
//        assertThat(actual.getAllReceiver().next(), is(aidReceiverMock));
//    }
//
//    @Test
//    public void shouldSendRequestActionAndWaitDone() throws Exception {
//        String expectedOntology = "expectedOntology";
//        AgentAction agentActionMock = mock(AgentAction.class);
//        doReturn(new Done()).when(agentProtocolAssistantSpy).sendActionAndWaitContent(aidReceiverMock, expectedOntology, agentActionMock);
//        agentProtocolAssistantSpy.sendActionAndWaitDone(aidReceiverMock, expectedOntology, agentActionMock);
//        verify(agentProtocolAssistantSpy).sendActionAndWaitContent(aidReceiverMock, expectedOntology, agentActionMock);
//    }
//
//    @Test
//    public void shouldSendRequestActionAndGetContent() throws Exception {
//        String expectedOntology = "expectedOntology";
//        Done expectedContent = new Done();
//
//        AgentAction agentActionMock = mock(AgentAction.class);
//        ACLMessage responseMock = mock(ACLMessage.class);
//
//        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
//        doReturn(expectedContent).when(contentManagerMock).extractContent(responseMock);
//        doReturn(responseMock).when(agentProtocolAssistantSpy).sendActionAndWaitMessage(aidReceiverMock, expectedOntology, agentActionMock);
//
//        ContentElement contentElement = agentProtocolAssistantSpy.sendActionAndWaitContent(aidReceiverMock, expectedOntology, agentActionMock);
//        assertThat(contentElement, is(expectedContent));
//
//        verify(agentProtocolAssistantSpy).sendActionAndWaitMessage(aidReceiverMock, expectedOntology, agentActionMock);
//        verify(contentManagerMock).extractContent(responseMock);
//    }
//
//    @Test
//    public void shouldSendRequest() throws Exception {
//        String expectedOntology = "expectedOntology";
//
//        AgentAction agentActionMock = mock(AgentAction.class);
//        ACLMessage requestMock = mock(ACLMessage.class);
//
//        doReturn(requestMock).when(agentProtocolAssistantSpy).createRequestAction(aidReceiverMock, expectedOntology);
//
//        ACLMessage contentElement = agentProtocolAssistantSpy.sendAction(aidReceiverMock, expectedOntology, agentActionMock);
//        assertThat(contentElement, is(requestMock));
//
//        verify(agentProtocolAssistantSpy).createRequestAction(aidReceiverMock, expectedOntology);
//        verify(contentManagerMock).fillContent(eq(requestMock), agentActionArgumentCaptor.capture());
//        verify(agentMock).send(requestMock);
//
//        AgentAction agentAction = agentActionArgumentCaptor.getValue();
//        assertThat(agentAction, is(instanceOf(Action.class)));
//
//        Action action = (Action) agentAction;
//        assertThat(action.getAction(), is(agentActionMock));
//        assertThat(action.getActor(), is(aidReceiverMock));
//    }
//
//    @Test
//    public void shouldSendRequestActionAnWaitMessage() throws Exception {
//        String expectedOntology = "expectedOntology";
//        String expectedReplyWith = "expectedReplyWith";
//
//        AgentAction agentActionMock = mock(AgentAction.class);
//        ACLMessage requestMock = mock(ACLMessage.class);
//        ACLMessage responseMock = mock(ACLMessage.class);
//
//        doReturn(requestMock).when(agentProtocolAssistantSpy).createRequestAction(aidReceiverMock, expectedOntology);
//        doReturn(expectedReplyWith).when(requestMock).getReplyWith();
//        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
//        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
//
//        ACLMessage contentElement = agentProtocolAssistantSpy.sendActionAndWaitMessage(aidReceiverMock, expectedOntology, agentActionMock);
//        assertThat(contentElement, is(responseMock));
//
//        verify(agentProtocolAssistantSpy).createRequestAction(aidReceiverMock, expectedOntology);
//        verify(contentManagerMock).fillContent(eq(requestMock), agentActionArgumentCaptor.capture());
//        verify(agentMock).send(requestMock);
//
//        AgentAction agentAction = agentActionArgumentCaptor.getValue();
//        assertThat(agentAction, is(instanceOf(Action.class)));
//
//        Action action = (Action) agentAction;
//        assertThat(action.getAction(), is(agentActionMock));
//        assertThat(action.getActor(), is(aidReceiverMock));
//
//        MessageTemplate expectedMessageTemplate = MessageTemplate.MatchInReplyTo(expectedReplyWith);
//        verify(agentMock).blockingReceive(messageTemplateArgumentCaptor.capture(), anyLong());
//        assertReflectionEquals(expectedMessageTemplate, messageTemplateArgumentCaptor.getValue());
//    }
//
//    @Test
//    public void shouldVerifyTimeoutControl() throws Exception {
//        long timeout = 10000L;
//        long delayInAgree = 2000L;
//        ACLMessage responseMock = mock(ACLMessage.class);
//
//        doReturn(ACLMessage.AGREE).doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
//        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
//        doReturn(new Done()).when(contentManagerMock).extractContent(responseMock);
//        doReturn(delayInAgree).when(stopWatchMock).getTime();
//
//        agentProtocolAssistantSpy.setTimeout(timeout);
//        agentProtocolAssistantSpy.sendActionAndWaitMessage(aidReceiverMock, "expectedOntology", mock(AgentAction.class));
//
//        InOrder inOrder = inOrder(stopWatchMock, agentMock);
//        inOrder.verify(stopWatchMock).reset();
//        inOrder.verify(stopWatchMock).start();
//        inOrder.verify(agentMock).blockingReceive(any(MessageTemplate.class), eq(timeout));
//        inOrder.verify(stopWatchMock).stop();
//        inOrder.verify(stopWatchMock).getTime();
//        inOrder.verify(agentMock).blockingReceive(any(MessageTemplate.class), eq(timeout - delayInAgree));
//    }
//
//    @Test
//    public void shouldRegisterOntologyIsNotExist() {
//        Ontology ontologyMock = mock(Ontology.class);
//        agentProtocolAssistantSpy.registerOntology(ontologyMock);
//        verify(contentManagerMock).registerOntology(ontologyMock);
//    }
//
//    @Test
//    public void shouldWaitForMessageAfterAgreeInSendActionAndWaitMessage() throws Exception {
//        ACLMessage responseMock = mock(ACLMessage.class);
//        doReturn(ACLMessage.AGREE)
//                .doReturn(ACLMessage.INFORM)
//                .when(responseMock)
//                .getPerformative();
//        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
//        doReturn(new Done()).when(contentManagerMock).extractContent(responseMock);
//        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), anyString(), any(AgentAction.class));
//        verify(agentMock, times(2)).blockingReceive(any(MessageTemplate.class), anyLong());
//    }
//
//    @Test
//    public void shouldThrowFillOntologyContentExceptionInSendRequestActionWhenErrorInFillContent() throws Exception {
//        String expectedMessage = "expectedMessage";
//        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
//        expectedException.expect(FillOntologyContentException.class);
//        expectedException.expectMessage(expectedMessage);
//        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), anyString(), any(AgentAction.class));
//    }
//
//    @Test
//    public void shouldThrowTimeoutExceptionInSendRequestActionWhenResponseIsNull() throws Exception {
//        String expectedMessage = "The agent did not respond";
//        doReturn(null).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
//        expectedException.expect(TimeoutRequestException.class);
//        expectedException.expectMessage(expectedMessage);
//        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), anyString(), any(AgentAction.class));
//    }
//
//    @Test
//    public void shouldThrowTimeoutExceptionInSendRequestActionWhenWaitInformAndResponseIsNull() throws Exception {
//        String expectedMessage = "The agent did not respond";
//        ACLMessage responseMock = mock(ACLMessage.class);
//        doReturn(ACLMessage.AGREE).when(responseMock).getPerformative();
//        doReturn(responseMock)
//                .doReturn(null)
//                .when(agentMock)
//                .blockingReceive(any(MessageTemplate.class), anyLong());
//        expectedException.expect(TimeoutRequestException.class);
//        expectedException.expectMessage(expectedMessage);
//        agentProtocolAssistantSpy.sendActionAndWaitMessage(any(AID.class), anyString(), any(AgentAction.class));
//    }
//
//    @Test
//    public void shouldThrowInvalidResponseExceptionInSendRequestActionWhenResponseIsNotInform() throws Exception {
//        expectedException.expect(InvalidResponseException.class);
//        expectedException.expectMessage("No inform response: " + ACLMessage.getPerformative(ACLMessage.NOT_UNDERSTOOD));
//        ACLMessage responseMock = mock(ACLMessage.class);
//        doReturn(ACLMessage.NOT_UNDERSTOOD).when(responseMock).getPerformative();
//        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
//        agentProtocolAssistantSpy.sendActionAndWaitContent(any(AID.class), anyString(), any(AgentAction.class));
//    }
//
//    @Test
//    public void shouldThrowExtractOntologyContentExceptionInSendRequestActionWhenErrorInExtractManager() throws Exception {
//        ACLMessage responseMock = mock(ACLMessage.class);
//        String expectedMessage = "expectedMessage";
//        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).extractContent(any(ACLMessage.class));
//        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
//        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
//        expectedException.expect(ExtractOntologyContentException.class);
//        expectedException.expectMessage(expectedMessage);
//        agentProtocolAssistantSpy.sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//    }
//
//    @Test
//    public void shouldThrowInvalidResponseExceptionInSendRequestActionWhenContentIsNotDone() throws Exception {
//        ACLMessage responseMock = mock(ACLMessage.class);
//        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
//        String expectedContent = "content";
//        doReturn(expectedContent).when(responseMock).getContent();
//        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
//        doReturn(mock(ContentElement.class)).when(contentManagerMock).extractContent(responseMock);
//        expectedException.expect(InvalidResponseException.class);
//        expectedException.expectMessage("Unknown notification: " + expectedContent);
//        agentProtocolAssistantSpy.sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//    }
//
//    @Test
//    public void shouldSendKillAgentMessage() {
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        AID aidToKillMock = mock(AID.class);
//        agentProtocolAssistantSpy.killAgent(aidToKillMock);
//        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidReceiverMock), eq(JADEManagementOntology.NAME), agentActionArgumentCaptor.capture());
//        ContentElement contentElement = agentActionArgumentCaptor.getValue();
//        assertThat(contentElement, is(instanceOf(KillAgent.class)));
//        KillAgent killAgent = (KillAgent) contentElement;
//        assertThat(killAgent.getAgent(), is(aidToKillMock));
//    }
//
//    @Test
//    public void shouldSendCreateAgentMessage() {
//        AID newAgentMock = mock(AID.class);
//
//        String expectedAgentName = "expectedAgentName";
//        Class<Agent> expectedAgentClass = Agent.class;
//        String arg1 = "arg1";
//
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        doReturn(newAgentMock).when(agentMock).getAID(expectedAgentName);
//
//        AID newAgent = agentProtocolAssistantSpy.createAgent(expectedAgentName, expectedAgentClass, Arrays.asList(arg1));
//        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidReceiverMock), eq(JADEManagementOntology.NAME), agentActionArgumentCaptor.capture());
//
//        ContentElement contentElement = agentActionArgumentCaptor.getValue();
//        assertThat(contentElement, is(instanceOf(CreateAgent.class)));
//
//        CreateAgent createAgent = (CreateAgent) contentElement;
//        assertThat(createAgent.getAgentName(), is(expectedAgentName));
//        assertThat(createAgent.getClassName(), is(Agent.class.getCanonicalName()));
//        assertThat(createAgent.getContainer(), is(containerIdMock));
//        assertThat(createAgent.getAllArguments().next(), is(arg1));
//        assertThat(newAgent, is(newAgentMock));
//    }
//
//    @Test
//    public void shouldSendCreateAgentWithNullArguments() {
//        String expectedAgentName = "expectedAgentName";
//        Class<Agent> expectedAgentClass = Agent.class;
//
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//
//        agentProtocolAssistantSpy.createAgent(expectedAgentName, expectedAgentClass, null);
//        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidReceiverMock), eq(JADEManagementOntology.NAME), agentActionArgumentCaptor.capture());
//
//        ContentElement contentElement = agentActionArgumentCaptor.getValue();
//        assertThat(contentElement, is(instanceOf(CreateAgent.class)));
//
//        CreateAgent createAgent = (CreateAgent) contentElement;
//        assertFalse(createAgent.getAllArguments().hasNext());
//    }
//
//    @Test
//    public void shouldSendCreateAgentWithoutArguments() {
//        String expectedAgentName = "expectedAgentName";
//        Class<Agent> expectedAgentClass = Agent.class;
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        agentProtocolAssistantSpy.createAgent(expectedAgentName, expectedAgentClass);
//        verify(agentProtocolAssistantSpy).createAgent(expectedAgentName, expectedAgentClass, null);
//    }
//
//    @Test
//    public void shouldCreateAgentWithRandomNameAndWithoutArguments() {
//        Class<Agent> expectedAgentClass = Agent.class;
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        agentProtocolAssistantSpy.createAgent(expectedAgentClass);
//        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, expectedAgentClass, null);
//    }
//
//    @Test
//    public void shouldCreateAgentWithRandomName() {
//        Class<Agent> expectedAgentClass = Agent.class;
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        List argumentsMock = mock(List.class);
//        agentProtocolAssistantSpy.createAgent(expectedAgentClass, argumentsMock);
//        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, expectedAgentClass, argumentsMock);
//    }
//
//    @Test
//    public void shouldSendCreateConfigurableAgentWithName() {
//        String expectedAgentName = "expectedAgentName";
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        agentProtocolAssistantSpy.createAgent(expectedAgentName);
//        verify(agentProtocolAssistantSpy).createAgent(expectedAgentName, ConfigurableAgent.class, null);
//    }
//
//    @Test
//    public void shouldSendCreateConfigurableAgent() {
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        agentProtocolAssistantSpy.createAgent();
//        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, ConfigurableAgent.class, null);
//    }
//
//    @Test
//    public void shouldSendCreateConfigurableAgentWithArguments() {
//        List argumentsMock = mock(List.class);
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        agentProtocolAssistantSpy.createAgent(argumentsMock);
//        verify(agentProtocolAssistantSpy).createAgent(RANDOM_STRING, ConfigurableAgent.class, argumentsMock);
//    }
//
//    @Test
//    public void shouldSendCreateConfigurableAgentWithArgumentsAndName() {
//        List argumentsMock = mock(List.class);
//        String expectedAgentName = "expectedAgentName";
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        agentProtocolAssistantSpy.createAgent(expectedAgentName, argumentsMock);
//        verify(agentProtocolAssistantSpy).createAgent(expectedAgentName, ConfigurableAgent.class, argumentsMock);
//    }
//
//    @Test
//    public void shouldAddBehaviour() {
//        AID aidConfigurableAgentMock = mock(AID.class);
//        String expectedBehaviourName = "expectedBehaviourName";
//
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//
//        String behaviourName = agentProtocolAssistantSpy.addBehaviour(aidConfigurableAgentMock, expectedBehaviourName, Behaviour.class);
//
//        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidConfigurableAgentMock), eq(ConfigurableOntology.ONTOLOGY_NAME), agentActionArgumentCaptor.capture());
//
//        ContentElement contentElement = agentActionArgumentCaptor.getValue();
//        assertThat(contentElement, is(instanceOf(AddBehaviour.class)));
//
//        AddBehaviour addBehaviour = (AddBehaviour) contentElement;
//        assertThat(addBehaviour.getClassName(), is(Behaviour.class.getCanonicalName()));
//        assertThat(addBehaviour.getName(), is(expectedBehaviourName));
//        assertThat(behaviourName, is(expectedBehaviourName));
//    }
//
//    @Test
//    public void shouldAddBehaviourWithoutName() {
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//        AID aidMock = mock(AID.class);
//        Class<Behaviour> expectedClass = Behaviour.class;
//        String behaviourName = agentProtocolAssistantSpy.addBehaviour(aidMock, expectedClass);
//        verify(agentProtocolAssistantSpy).addBehaviour(aidMock, RANDOM_STRING, expectedClass);
//        assertThat(behaviourName, is(RANDOM_STRING));
//    }
//
//    @Test
//    public void shouldRemoveBehaviour() {
//        AID aidConfigurableAgentMock = mock(AID.class);
//        String expectedBehaviourName = "expectedBehaviourName";
//
//        doNothing().when(agentProtocolAssistantSpy).sendActionAndWaitDone(any(AID.class), anyString(), any(AgentAction.class));
//
//        agentProtocolAssistantSpy.removeBehaviour(aidConfigurableAgentMock, expectedBehaviourName);
//
//        verify(agentProtocolAssistantSpy).sendActionAndWaitDone(eq(aidConfigurableAgentMock), eq(ConfigurableOntology.ONTOLOGY_NAME), agentActionArgumentCaptor.capture());
//
//        ContentElement contentElement = agentActionArgumentCaptor.getValue();
//        assertThat(contentElement, is(instanceOf(RemoveBehaviour.class)));
//
//        RemoveBehaviour removeBehaviour = (RemoveBehaviour) contentElement;
//        assertThat(removeBehaviour.getName(), is(expectedBehaviourName));
//    }
//
//
//    @Test
//    public void shouldInvokeKillContainerFromAgent() throws Exception {
//        agentProtocolAssistant.killContainer();
//        verify(contentManagerMock).fillContent(messageArgumentCaptor.capture(), agentActionArgumentCaptor.capture());
//        verify(agentMock).send(messageArgumentCaptor.getValue());
//
//        KillContainer content = new KillContainer();
//        content.setContainer(containerIdMock);
//        Action expectedAction = new Action(aidReceiverMock, content);
//
//        assertReflectionEquals(expectedAction, agentActionArgumentCaptor.getValue());
//    }
//
//    @Test
//    public void shouldThrowFillOntologyContentExceptionInKillContainerWhenErrorInFillContent() throws Exception {
//        String expectedMessage = "expectedMessage";
//        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
//        expectedException.expect(FillOntologyContentException.class);
//        expectedException.expectMessage(expectedMessage);
//        agentProtocolAssistant.killContainer();
//    }
//
//    @Test
//    public void shouldInvokeShutDownFromAgent() throws Exception {
//        agentProtocolAssistant.shutDownPlatform();
//        verify(contentManagerMock).fillContent(messageArgumentCaptor.capture(), agentActionArgumentCaptor.capture());
//        verify(agentMock).send(messageArgumentCaptor.getValue());
//
//        ShutdownPlatform content = new ShutdownPlatform();
//        Action expectedAction = new Action(aidReceiverMock, content);
//
//        assertReflectionEquals(expectedAction, agentActionArgumentCaptor.getValue());
//    }
//
//    @Test
//    public void shouldThrowFillOntologyContentExceptionInShutDownWhenErrorInFillContent() throws Exception {
//        String expectedMessage = "expectedMessage";
//        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
//        expectedException.expect(FillOntologyContentException.class);
//        expectedException.expectMessage(expectedMessage);
//        agentProtocolAssistant.shutDownPlatform();
//    }
//
//    @Test
//    public void shouldThrowFillOntologyContentExceptionInSendActionWhenErrorInFillContent() throws Exception {
//        String expectedMessage = "expectedMessage";
//        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
//        expectedException.expect(FillOntologyContentException.class);
//        expectedException.expectMessage(expectedMessage);
//        agentProtocolAssistant.sendAction(mock(AID.class), "", mock(AgentAction.class));
//    }

}