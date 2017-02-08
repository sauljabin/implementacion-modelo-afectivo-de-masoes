/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class RequesterGuiTest {

    private JComboBox actionsComboBoxMock;
    private JLabel senderAgentLabelMock;
    private RequesterGui requesterGuiSpy;
    private RequesterGui requesterGui;
    private JButton sendRequestButtonMock;
    private JTextField receiverAgentTextField;

    @Before
    public void setUp() throws Exception {
        sendRequestButtonMock = mock(JButton.class);
        senderAgentLabelMock = mock(JLabel.class);
        actionsComboBoxMock = mock(JComboBox.class);
        receiverAgentTextField = mock(JTextField.class);

        requesterGui = new RequesterGui();
        setFieldValue(requesterGui, "sendRequestButton", sendRequestButtonMock);
        setFieldValue(requesterGui, "senderAgentLabel", senderAgentLabelMock);
        setFieldValue(requesterGui, "actionsComboBox", actionsComboBoxMock);
        setFieldValue(requesterGui, "receiverAgentTextField", receiverAgentTextField);

        requesterGuiSpy = spy(requesterGui);
        doNothing().when(requesterGuiSpy).setVisible(anyBoolean());
        doNothing().when(requesterGuiSpy).dispose();
    }

    @Test
    public void shouldSetupViewConfiguration() {
        assertThat(requesterGuiSpy.getTitle(), is("Requester GUI"));
        assertThat(requesterGuiSpy.getSize().getWidth(), is(1024.));
        assertThat(requesterGuiSpy.getSize().getHeight(), is(768.));
        assertThat(requesterGuiSpy.getDefaultCloseOperation(), is(WindowConstants.DO_NOTHING_ON_CLOSE));
        assertThat(requesterGuiSpy.getLayout(), is(IsInstanceOf.instanceOf(BorderLayout.class)));
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
        verify(actionsComboBoxMock).addActionListener(actionListenerMock);
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
    public void shouldGetReceiverAgentName() {
        String expectedText = "expectedText";
        doReturn(expectedText).when(receiverAgentTextField).getText();
        assertThat(requesterGui.getReceiverAgentName(), is(expectedText));
    }


}
