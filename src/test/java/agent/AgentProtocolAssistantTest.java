/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.configurable.ConfigurableOntology;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import protocol.ExtractOntologyContentException;
import protocol.FillOntologyContentException;
import util.StringGenerator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
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
    private AgentProtocolAssistant agentProtocolAssistant;
    private Agent agentMock;
    private AID aidAmsMock;
    private AID aidMock;
    private StringGenerator stringGeneratorMock;
    private ContentManager contentManagerMock;
    private ContainerID containerIdMock;
    private AgentProtocolAssistant agentProtocolAssistantSpy;

    @Before
    public void setUp() throws Exception {
        agentActionArgumentCaptor = ArgumentCaptor.forClass(AgentAction.class);
        messageTemplateArgumentCaptor = ArgumentCaptor.forClass(MessageTemplate.class);

        agentMock = mock(Agent.class);
        aidMock = mock(AID.class);
        aidAmsMock = mock(AID.class);
        stringGeneratorMock = mock(StringGenerator.class);
        contentManagerMock = mock(ContentManager.class);
        containerIdMock = mock(ContainerID.class);

        agentProtocolAssistant = new AgentProtocolAssistant(agentMock);

        doReturn(aidMock).when(agentMock).getAID();
        doReturn(aidAmsMock).when(agentMock).getAMS();
        doReturn(RANDOM_STRING).when(stringGeneratorMock).getString(anyInt());
        doReturn(containerIdMock).when(agentMock).here();

        setFieldValue(agentProtocolAssistant, "stringGenerator", stringGeneratorMock);
        setFieldValue(agentProtocolAssistant, "contentManager", contentManagerMock);

        agentProtocolAssistantSpy = spy(agentProtocolAssistant);
    }

    @Test
    public void shouldConfigContentManager() throws Exception {
        agentProtocolAssistant = new AgentProtocolAssistant(agentMock);
        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames()[0], is(JADEManagementOntology.NAME));
        assertThat(agentProtocolAssistant.getContentManager().getOntologyNames()[1], is(ConfigurableOntology.ONTOLOGY_NAME));
        assertThat(agentProtocolAssistant.getContentManager().getLanguageNames()[0], is(FIPANames.ContentLanguage.FIPA_SL));
    }

    @Test
    public void shouldCreateCorrectMessageRequest() {
        String expectedOntology = "expectedOntology";
        ACLMessage actual = agentProtocolAssistant.createRequestMessage(expectedOntology, aidAmsMock);
        assertThat(actual.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(actual.getOntology(), is(expectedOntology));
        assertThat(actual.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(actual.getSender(), is(aidMock));
        assertThat(actual.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actual.getConversationId(), is(RANDOM_STRING));
        assertThat(actual.getReplyWith(), is(RANDOM_STRING));
        assertThat(actual.getAllReceiver().next(), is(aidAmsMock));
    }

    @Test
    public void shouldSendRequestAction() throws Exception {
        String expectedOntology = "expectedOntology";
        String expectedReplyWith = "expectedReplyWith";

        AgentAction agentActionMock = mock(AgentAction.class);
        ACLMessage requestMock = mock(ACLMessage.class);
        ACLMessage responseMock = mock(ACLMessage.class);

        doReturn(requestMock).when(agentProtocolAssistantSpy).createRequestMessage(expectedOntology, aidAmsMock);
        doReturn(expectedReplyWith).when(requestMock).getReplyWith();
        doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        doReturn(new Done()).when(contentManagerMock).extractContent(responseMock);

        agentProtocolAssistantSpy.sendRequestAction(expectedOntology, agentActionMock, aidAmsMock);

        verify(agentProtocolAssistantSpy).createRequestMessage(expectedOntology, aidAmsMock);
        verify(contentManagerMock).fillContent(requestMock, agentActionMock);
        verify(agentMock).send(requestMock);

        MessageTemplate expectedMessageTemplate = MessageTemplate.MatchInReplyTo(expectedReplyWith);
        verify(agentMock).blockingReceive(messageTemplateArgumentCaptor.capture(), anyLong());
        assertReflectionEquals(expectedMessageTemplate, messageTemplateArgumentCaptor.getValue());

        verify(contentManagerMock).extractContent(responseMock);
    }

    @Test
    public void shouldWaitForInformAfterAgreeInSendRequestAction() throws Exception {
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.AGREE)
                .doReturn(ACLMessage.INFORM)
                .when(responseMock)
                .getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        doReturn(new Done()).when(contentManagerMock).extractContent(responseMock);
        agentProtocolAssistantSpy.sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
        verify(agentMock, times(2)).blockingReceive(any(MessageTemplate.class), anyLong());
    }

    @Test
    public void shouldThrowFillOntologyContentExceptionInSendRequestActionWhenErrorInFillContent() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(AgentAction.class));
        expectedException.expect(FillOntologyContentException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistantSpy.sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
    }

    @Test
    public void shouldThrowTimeoutExceptionInSendRequestActionWhenResponseIsNull() throws Exception {
        String expectedMessage = "The agent did not respond";
        doReturn(null).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expect(TimeoutResponseException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistantSpy.sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
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
        expectedException.expect(TimeoutResponseException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistantSpy.sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
    }

    @Test
    public void shouldThrowInvalidResponseExceptionInSendRequestActionWhenResponseIsNotInform() throws Exception {
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("No inform response: " + ACLMessage.getPerformative(ACLMessage.NOT_UNDERSTOOD));
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.NOT_UNDERSTOOD).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentProtocolAssistantSpy.sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
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
        agentProtocolAssistantSpy.sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
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
        agentProtocolAssistantSpy.sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
    }

    @Test
    public void shouldSendKillContainerMessage() throws Exception {
        ACLMessage requestMock = mock(ACLMessage.class);
        doReturn(requestMock).when(agentProtocolAssistantSpy).createRequestMessage(JADEManagementOntology.NAME, aidAmsMock);
        agentProtocolAssistantSpy.killContainer();
        verify(agentProtocolAssistantSpy).createRequestMessage(JADEManagementOntology.NAME, aidAmsMock);
        verify(contentManagerMock).fillContent(eq(requestMock), agentActionArgumentCaptor.capture());
        verify(agentMock).send(requestMock);

        ContentElement contentElement = agentActionArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(KillContainer.class)));

        KillContainer killContainer = (KillContainer) contentElement;
        assertThat(killContainer.getContainer(), is(containerIdMock));
    }

    @Test
    public void shouldThrowFillOntologyContentExceptionWhenErrorInKillContainer() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(ContentElement.class));
        expectedException.expect(FillOntologyContentException.class);
        expectedException.expectMessage(expectedMessage);
        agentProtocolAssistant.killContainer();
    }

    @Test
    public void shouldSendKillAgentMessage() {
        doNothing().when(agentProtocolAssistantSpy).sendRequestAction(anyString(), any(AgentAction.class), any(AID.class));
        AID aidToKillMock = mock(AID.class);
        agentProtocolAssistantSpy.killAgent(aidToKillMock);
        verify(agentProtocolAssistantSpy).sendRequestAction(eq(JADEManagementOntology.NAME), agentActionArgumentCaptor.capture(), eq(aidAmsMock));
        ContentElement contentElement = agentActionArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(KillAgent.class)));
        KillAgent killAgent = (KillAgent) contentElement;
        assertThat(killAgent.getAgent(), is(aidToKillMock));
    }

}