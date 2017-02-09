/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import agent.AgentLogger;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
import ontology.settings.GetAllSettings;
import ontology.settings.GetSetting;
import ontology.settings.SettingsOntology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.swing.*"})
@PrepareForTest(Agent.class)
public class RequesterGuiAgentTest {

    private static final String RECEIVER_AGENT_NAME = "receiverAgentName";
    private static final String SENDER_AGENT_NAME = "senderAgentName";
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private RequesterGuiAgent requesterGuiAgent;
    private RequesterGuiAgent requesterGuiAgentSpy;
    private RequesterGui requesterGuiMock;
    private AID senderAgentAID;
    private AID receiverAgentAID;
    private AgentLogger logguerMock;

    @Before
    public void setUp() throws Exception {
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);

        requesterGuiMock = mock(RequesterGui.class);
        logguerMock = mock(AgentLogger.class);

        requesterGuiAgent = new RequesterGuiAgent();
        setFieldValue(requesterGuiAgent, "requesterGui", requesterGuiMock);
        setFieldValue(requesterGuiAgent, "logger", logguerMock);

        requesterGuiAgentSpy = spy(requesterGuiAgent);

        senderAgentAID = new AID(SENDER_AGENT_NAME, AID.ISGUID);
        receiverAgentAID = new AID(RECEIVER_AGENT_NAME, AID.ISGUID);
        doReturn(senderAgentAID).when(requesterGuiAgentSpy).getAID();
        doReturn(receiverAgentAID).when(requesterGuiAgentSpy).getAID(RECEIVER_AGENT_NAME);

        doReturn(RECEIVER_AGENT_NAME).when(requesterGuiMock).getReceiverAgentName();
    }

    @Test
    public void shouldInvokeSetupAndShowGuiWhenStartAgent() {
        requesterGuiAgent.setup();
        verify(requesterGuiMock).showGui();
    }

    @Test
    public void shouldAddResponseHandlerBehaviour() {
        requesterGuiAgentSpy.setup();
        verify(requesterGuiAgentSpy).addBehaviour(isA(GuiResponseHandlerBehaviour.class));
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        requesterGuiAgent.takeDown();
        verify(requesterGuiMock).closeGui();
    }

    @Test
    public void shouldInvokeAgentDoDeleteOnEventClose() {
        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.CLOSE_WINDOW.getInt());
        doNothing().when(requesterGuiAgentSpy).doDelete();
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(requesterGuiAgentSpy).doDelete();
    }

    @Test
    public void shouldSetSenderAgentName() {
        String expectedName = "expectedName";
        doReturn(expectedName).when(requesterGuiAgentSpy).getLocalName();
        requesterGuiAgentSpy.setup();
        verify(requesterGuiMock).setSenderAgentName(expectedName);
    }

    @Test
    public void shouldLogExceptionWhenErrorInEventMethod() {
        RuntimeException expectedException = new RuntimeException("message");
        doThrow(expectedException).when(requesterGuiMock).logMessage(any(ACLMessage.class));
        doReturn(RequesterGuiAction.GET_ALL_SETTINGS).when(requesterGuiMock).getSelectedAction();

        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(logguerMock).exception(requesterGuiAgentSpy, expectedException);
    }

    @Test
    public void shouldClearMessageLogsInWindows() {
        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.CLEAR_MESSAGE_LOGS.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(requesterGuiMock).clearMessagesLog();
    }

    @Test
    public void shouldSendGetAllSettingsToAgent() throws Exception {
        doReturn(RequesterGuiAction.GET_ALL_SETTINGS).when(requesterGuiMock).getSelectedAction();

        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);

        ContentElement contentElement = testRequestAction();

        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(GetAllSettings.class)));
    }

    @Test
    public void shouldSendGetASettingToAgent() throws Exception {
        String expectedKey = "expectedSetting";
        doReturn(expectedKey).when(requesterGuiMock).getKeySetting();
        doReturn(RequesterGuiAction.GET_SETTING).when(requesterGuiMock).getSelectedAction();

        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);

        ContentElement contentElement = testRequestAction();

        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(GetSetting.class)));

        GetSetting getSetting = (GetSetting) action.getAction();
        assertThat(getSetting.getKey(), is(expectedKey));
    }

    @Test
    public void shouldSendSimpleContentToAgent() throws Exception {
        String expectedContent = "expectedContent";
        doReturn(expectedContent).when(requesterGuiMock).getSimpleContent();
        doReturn(RequesterGuiAction.SEND_SIMPLE_CONTENT).when(requesterGuiMock).getSelectedAction();

        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);

        verify(requesterGuiMock).logMessage(messageArgumentCaptor.capture());
        verify(requesterGuiAgentSpy).send(messageArgumentCaptor.getValue());

        ACLMessage message = messageArgumentCaptor.getValue();
        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(message.getReplyWith(), is(notNullValue()));
        assertThat(message.getConversationId(), is(notNullValue()));
        assertThat(message.getSender(), is(senderAgentAID));
        assertThat(message.getAllReceiver().next(), is(receiverAgentAID));
        assertThat(message.getContent(), is(expectedContent));
    }

    private ContentElement testRequestAction() throws Codec.CodecException, OntologyException {
        SettingsOntology ontology = SettingsOntology.getInstance();
        SemanticLanguage language = SemanticLanguage.getInstance();

        verify(requesterGuiMock).logMessage(messageArgumentCaptor.capture());
        verify(requesterGuiAgentSpy).send(messageArgumentCaptor.getValue());

        ACLMessage message = messageArgumentCaptor.getValue();

        ContentManager contentManager = new ContentManager();
        contentManager.registerOntology(ontology);
        contentManager.registerLanguage(language);

        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(message.getOntology(), is(ontology.getName()));
        assertThat(message.getLanguage(), is(language.getName()));
        assertThat(message.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(message.getReplyWith(), is(notNullValue()));
        assertThat(message.getConversationId(), is(notNullValue()));
        assertThat(message.getSender(), is(senderAgentAID));
        assertThat(message.getAllReceiver().next(), is(receiverAgentAID));

        ContentElement contentElement = contentManager.extractContent(message);
        assertThat(contentElement, is(instanceOf(Action.class)));
        return contentElement;
    }

}