/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class RequesterGuiTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JComboBox actionsComboBoxMock;
    private JLabel senderAgentLabelMock;
    private RequesterGui requesterGuiSpy;
    private RequesterGui requesterGui;
    private JButton sendRequestButtonMock;
    private JTextPane messageTextPaneMock;
    private JButton saveMessageLogsButtonMock;
    private JButton clearMessageLogsButtonMock;
    private JPanel dynamicCanvasPanelMock;

    @Before
    public void setUp() throws Exception {
        sendRequestButtonMock = mock(JButton.class);
        senderAgentLabelMock = mock(JLabel.class);
        actionsComboBoxMock = mock(JComboBox.class);
        messageTextPaneMock = mock(JTextPane.class);
        saveMessageLogsButtonMock = mock(JButton.class);
        clearMessageLogsButtonMock = mock(JButton.class);
        dynamicCanvasPanelMock = mock(JPanel.class);

        requesterGui = new RequesterGui();
        setFieldValue(requesterGui, "sendRequestButton", sendRequestButtonMock);
        setFieldValue(requesterGui, "saveMessagesLogButton", saveMessageLogsButtonMock);
        setFieldValue(requesterGui, "clearMessagesLogButton", clearMessageLogsButtonMock);
        setFieldValue(requesterGui, "senderAgentLabel", senderAgentLabelMock);
        setFieldValue(requesterGui, "actionsComboBox", actionsComboBoxMock);
        setFieldValue(requesterGui, "messageTextPane", messageTextPaneMock);
        setFieldValue(requesterGui, "dynamicCanvasPanel", dynamicCanvasPanelMock);

        requesterGuiSpy = spy(requesterGui);
        doNothing().when(requesterGuiSpy).setVisible(anyBoolean());
        doNothing().when(requesterGuiSpy).dispose();
    }

    @Test
    public void shouldSetupViewConfiguration() {
        assertThat(requesterGuiSpy.getDefaultCloseOperation(), is(WindowConstants.DO_NOTHING_ON_CLOSE));
        assertThat(requesterGuiSpy.getLayout(), is(instanceOf(BorderLayout.class)));
    }

    @Test
    public void shouldShowWindow() {
        requesterGuiSpy.showGui();
        verify(requesterGuiSpy).setVisible(true);
    }

    @Test
    public void shouldCloseWindow() {
        requesterGuiSpy.closeGui();
        verify(requesterGuiSpy).setVisible(false);
        verify(requesterGuiSpy).dispose();
    }

    @Test
    public void shouldAddActionListenerToButton() {
        ActionListener actionListenerMock = mock(ActionListener.class);
        requesterGui.addActionListener(actionListenerMock);
        verify(sendRequestButtonMock).addActionListener(actionListenerMock);
        verify(clearMessageLogsButtonMock).addActionListener(actionListenerMock);
        verify(saveMessageLogsButtonMock).addActionListener(actionListenerMock);
    }

    @Test
    public void shouldSetSenderAgentName() {
        String expectedName = "expectedName";
        requesterGui.setSenderAgentName(expectedName);
        verify(senderAgentLabelMock).setText(expectedName);
    }

    @Test
    public void shouldReturnSelectedItem() {
        RequesterGuiAction requesterGuiAction = RequesterGuiAction.ADD_BEHAVIOUR;
        doReturn(requesterGuiAction).when(actionsComboBoxMock).getSelectedItem();
        assertThat(requesterGui.getSelectedAction(), is(requesterGuiAction));
    }

    @Test
    public void shouldLogMessage() throws Exception {
        StyledDocument styledDocumentMock = mock(StyledDocument.class);
        doReturn(styledDocumentMock).when(messageTextPaneMock).getStyledDocument();

        ACLMessage messageMock = mock(ACLMessage.class);
        String expectedMessageToString = "expectedMesage";
        String expectedConversationId = "expectedConversationId";
        doReturn(expectedMessageToString).when(messageMock).toString();
        doReturn(expectedConversationId).when(messageMock).getConversationId();

        requesterGui.logMessage(messageMock);

        verify(styledDocumentMock).insertString(anyInt(), contains(expectedConversationId), any(AttributeSet.class));
        verify(styledDocumentMock).insertString(anyInt(), eq(expectedMessageToString + "\n\n"), any(AttributeSet.class));
    }

    @Test
    public void shouldClearLogMessage() {
        requesterGui.clearMessagesLog();
        verify(messageTextPaneMock).setText("");
    }

    @Test
    public void shouldThrowGuiExceptionInLogMessageWhenErrorInPane() throws Exception {
        String expectedMessage = "expectedMessage";
        expectedException.expectMessage(expectedMessage);
        expectedException.expect(GuiException.class);

        StyledDocument styledDocumentMock = mock(StyledDocument.class);
        doReturn(styledDocumentMock).when(messageTextPaneMock).getStyledDocument();
        doThrow(new RuntimeException(expectedMessage)).when(styledDocumentMock).insertString(anyInt(), anyString(), any(AttributeSet.class));

        ACLMessage messageMock = mock(ACLMessage.class);
        requesterGui.logMessage(messageMock);
    }

    @Test
    public void shouldRemoveAllWhenActionIsGetAllSettings() {
        requesterGuiSpy.setGetAllSettingsActionComponents();
        testDynamicContentRepaint();
    }

    @Test
    public void shouldRemoveAllWhenActionIsGetEmotionalState() {
        requesterGuiSpy.setGetEmotionalStateActionComponents();
        testDynamicContentRepaint();
    }

    @Test
    public void shouldAddKeyWhenActionIsGetSetting() {
        requesterGuiSpy.setGetSettingActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddSimpleContentWhenActionIsSendSimpleContent() {
        requesterGuiSpy.setSimpleContentActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddBehaviourNameAndClassNameWhenActionIsAddBehavior() {
        requesterGuiSpy.setAddBehaviourActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddBehaviourNameWhenActionIsRemoveBehavior() {
        requesterGuiSpy.setRemoveBehaviourActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddActorAndActionWhenActionIsEvaluateActionStimulus() {
        requesterGuiSpy.setEvaluateActionStimulusComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddActorAndActionWhenActionIsNotifyAction() {
        requesterGuiSpy.setNotifyActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddAgentNameWhenActionIsGetServices() {
        requesterGuiSpy.setGetServicesActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldRemoveAllWhenActionIsGetAgents() {
        requesterGuiSpy.setGetAgentsActionComponents();
        testDynamicContentRepaint();
    }

    @Test
    public void shouldAddServiceNameWhenActionIsSearchAgent() {
        requesterGuiSpy.setSearchAgentActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddAgentNameWhenActionIsDeregisterAgent() {
        requesterGuiSpy.setDeregisterAgentActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddServiceNameWhenActionIsRegisterAgent() {
        requesterGuiSpy.setRegisterAgentActionComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddActorAndActionAndPropertiesWhenActionIsEvaluateObjectStimulus() {
        requesterGuiSpy.setEvaluateObjectStimulusComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(3)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JScrollPane.class), anyString());
    }

    @Test
    public void shouldAddCreatorAndObjectAndPropertiesWhenActionIsCreateObjectStimulus() {
        requesterGuiSpy.setCreateObjectStimulusComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(3)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JScrollPane.class), anyString());
    }

    @Test
    public void shouldAddCreatorAndObjectAndPropertiesWhenActionIsUpdateObjectStimulus() {
        requesterGuiSpy.setUpdateObjectStimulusComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(3)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
        verify(dynamicCanvasPanelMock).add(isA(JScrollPane.class), anyString());
    }

    @Test
    public void shouldAddCreatorAndObjectWhenActionIsDeleteObjectStimulus() {
        requesterGuiSpy.setDeleteObjectStimulusComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
    }

    @Test
    public void shouldAddCreatorAndObjectWhenActionIsGetObjectStimulus() {
        requesterGuiSpy.setGetObjectStimulusComponents();
        testDynamicContentRepaint();
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JLabel.class), anyString());
        verify(dynamicCanvasPanelMock, times(2)).add(isA(JTextField.class), anyString());
    }

    private void testDynamicContentRepaint() {
        verify(dynamicCanvasPanelMock).removeAll();
        verify(requesterGuiSpy).revalidate();
        verify(requesterGuiSpy).repaint();
    }

}
