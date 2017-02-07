/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.core.Agent;
import jade.gui.GuiEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.swing.*"})
@PrepareForTest(Agent.class)
public class RequesterGuiAgentTest {

    private RequesterGuiAgent requesterGuiAgent;
    private RequesterGuiAgent requesterGuiAgentSpy;
    private RequesterGui requesterGuiMock;

    @Before
    public void setUp() throws Exception {
        requesterGuiMock = mock(RequesterGui.class);
        requesterGuiAgent = new RequesterGuiAgent();
        setFieldValue(requesterGuiAgent, "requesterGui", requesterGuiMock);
        requesterGuiAgentSpy = spy(requesterGuiAgent);
    }

    @Test
    public void shouldInvokeSetupAndShowGuiWhenStartAgent() {
        requesterGuiAgent.setup();
        verify(requesterGuiMock).showGui();
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        requesterGuiAgent.takeDown();
        verify(requesterGuiMock).closeGui();
    }

    @Test
    public void shouldInvokeAgentDoDeleteOnEventClose() {
        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiAction.CLOSE_WINDOW.getInt());
        doNothing().when(requesterGuiAgentSpy).doDelete();
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(requesterGuiAgentSpy).doDelete();
    }

    @Test
    public void shouldSetSenderAgentName() {
        String expectedName = "expectedName";
        doReturn(expectedName).when(requesterGuiAgentSpy).getName();
        requesterGuiAgentSpy.setup();
        verify(requesterGuiMock).setSenderAgentName(expectedName);
    }

}