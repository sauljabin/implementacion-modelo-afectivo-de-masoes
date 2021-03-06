/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import jade.gui.GuiEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class RequesterGuiListenerTest {

    private RequesterGuiListener requesterGuiListener;
    private RequesterGui requesterGuiMock;
    private ArgumentCaptor<GuiEvent> guiEventArgumentCaptor;
    private RequesterGuiAgent requesterGuiAgentMock;

    @Before
    public void setUp() {
        guiEventArgumentCaptor = ArgumentCaptor.forClass(GuiEvent.class);
        requesterGuiMock = mock(RequesterGui.class);
        requesterGuiAgentMock = mock(RequesterGuiAgent.class);
        requesterGuiListener = new RequesterGuiListener(requesterGuiAgentMock, requesterGuiMock);
    }

    @Test
    public void shouldSetUpWindowsListenerListener() {
        verify(requesterGuiMock).addWindowListener(requesterGuiListener);
        verify(requesterGuiMock).addActionListener(requesterGuiListener);
    }

    @Test
    public void shouldInvokeOnGuiEvent() {
        GuiEvent expectedGuiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.CLOSE_WINDOW.getInt());

        WindowEvent windowEventMock = mock(WindowEvent.class);
        doReturn(requesterGuiMock).when(windowEventMock).getSource();

        requesterGuiListener.windowClosing(windowEventMock);

        verify(requesterGuiAgentMock).postGuiEvent(guiEventArgumentCaptor.capture());
        assertReflectionEquals(expectedGuiEvent, guiEventArgumentCaptor.getValue());
    }

    @Test
    public void shouldInvokeOnGuiEventWhenUserClicks() {
        GuiEvent expectedGuiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());

        ActionEvent actionEvent = mock(ActionEvent.class);
        doReturn(RequesterGuiEvent.SEND_MESSAGE.toString()).when(actionEvent).getActionCommand();

        requesterGuiListener.actionPerformed(actionEvent);

        verify(requesterGuiAgentMock).postGuiEvent(guiEventArgumentCaptor.capture());
        assertReflectionEquals(expectedGuiEvent, guiEventArgumentCaptor.getValue());
    }

}