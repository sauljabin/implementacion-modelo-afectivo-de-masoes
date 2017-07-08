/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import gui.requester.RequesterGuiEvent;
import jade.gui.GuiEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.event.WindowEvent;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class AgentStateGuiListenerTest {

    private AgentStateGuiListener agentStateGuiListener;
    private AgentStateGui agentStateGuiMock;
    private ArgumentCaptor<GuiEvent> guiEventArgumentCaptor;
    private AgentStateGuiAgent agentStateGuiAgentMock;

    @Before
    public void setUp() {
        guiEventArgumentCaptor = ArgumentCaptor.forClass(GuiEvent.class);
        agentStateGuiMock = mock(AgentStateGui.class);
        agentStateGuiAgentMock = mock(AgentStateGuiAgent.class);
        agentStateGuiListener = new AgentStateGuiListener(agentStateGuiAgentMock, agentStateGuiMock);
    }

    @Test
    public void shouldSetUpWindowsListenerListener() {
        verify(agentStateGuiMock).addWindowListener(agentStateGuiListener);
    }

    @Test
    public void shouldInvokeOnGuiEvent() {
        GuiEvent expectedGuiEvent = new GuiEvent(agentStateGuiMock, RequesterGuiEvent.CLOSE_WINDOW.getInt());

        WindowEvent windowEventMock = mock(WindowEvent.class);
        doReturn(agentStateGuiMock).when(windowEventMock).getSource();

        agentStateGuiListener.windowClosing(windowEventMock);

        verify(agentStateGuiAgentMock).postGuiEvent(guiEventArgumentCaptor.capture());
        assertReflectionEquals(expectedGuiEvent, guiEventArgumentCaptor.getValue());
    }

}