/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.agent;

import gui.view.RequesterGui;
import gui.view.RequesterGuiEvent;
import jade.gui.GuiEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class RequesterGuiAgentTest {

    private RequesterGuiAgent requesterGuiAgent;
    private RequesterGuiAgent requesterGuiAgentSpy;
    private RequesterGui requesterGuiMock;

    @Before
    public void setUp() throws Exception {
        requesterGuiAgent = new RequesterGuiAgent();
        requesterGuiMock = mock(RequesterGui.class);
        setFieldValue(requesterGuiAgent, "requesterGui", requesterGuiMock);
        requesterGuiAgentSpy = spy(requesterGuiAgent);
    }

    @Test
    public void shouldInvokeSetupAndShowGuiWhenStartAgent() {
        InOrder inOrder = inOrder(requesterGuiMock);
        requesterGuiAgent.setup();
        inOrder.verify(requesterGuiMock).setUp();
        inOrder.verify(requesterGuiMock).showGui();
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        requesterGuiAgent.takeDown();
        verify(requesterGuiMock).closeGui();
    }

    @Test
    public void shouldInvokeAgentDoDeleteOnEventClose() {
        GuiEvent guiEvent = new GuiEvent(mock(Object.class), RequesterGuiEvent.CLOSE_WINDOW.getInt());
        doNothing().when(requesterGuiAgentSpy).doDelete();
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(requesterGuiAgentSpy).doDelete();
    }

}