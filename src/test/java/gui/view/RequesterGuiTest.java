/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.view;

import gui.agent.RequesterGuiAgent;
import jade.gui.GuiEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.swing.*;
import java.awt.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class RequesterGuiTest {

    private RequesterGui requesterGui;
    private RequesterGui requesterGuiSpy;
    private RequesterGui requesterGuiMock;
    private ArgumentCaptor<JPanel> panelArgumentCaptor;
    private RequesterGuiAgent requesterGuiAgentMock;
    private ArgumentCaptor<GuiEvent> guiEventArgumentCaptor;

    @Before
    public void setUp() {
        guiEventArgumentCaptor = ArgumentCaptor.forClass(GuiEvent.class);
        panelArgumentCaptor = ArgumentCaptor.forClass(JPanel.class);
        requesterGuiAgentMock = mock(RequesterGuiAgent.class);
        requesterGui = new RequesterGui(requesterGuiAgentMock);
        requesterGuiSpy = spy(requesterGui);
        requesterGuiMock = mock(RequesterGui.class);
    }

    @Test
    public void shouldInvokeSetVisible() {
        doCallRealMethod().when(requesterGuiMock).showGui();
        requesterGuiMock.showGui();
        verify(requesterGuiMock).setVisible(true);
    }

    @Test
    public void shouldCloseWindow() {
        doCallRealMethod().when(requesterGuiMock).closeGui();
        requesterGuiMock.closeGui();
        verify(requesterGuiMock).setVisible(false);
        verify(requesterGuiMock).dispose();
    }

    @Test
    public void shouldInvokeOnGuiEvent() {
        GuiEvent expectedGuiEvent = new GuiEvent(mock(Object.class), RequesterGuiEvent.CLOSE_WINDOW.getInt());
        requesterGuiSpy.onGuiEvent(expectedGuiEvent);
        verify(requesterGuiAgentMock).postGuiEvent(eq(expectedGuiEvent));
    }

    @Test
    public void shouldSetupViewConfiguration() {
        requesterGuiSpy.setUp();
        verify(requesterGuiSpy).setTitle("Requester GUI");
        verify(requesterGuiSpy).setSize(1024, 768);
        verify(requesterGuiSpy).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        verify(requesterGuiSpy).setLayout(any(BorderLayout.class));
        verify(requesterGuiSpy).setLocationRelativeTo(requesterGuiSpy);
        verify(requesterGuiSpy).addWindowListener(isA(RequesterGuiListener.class));
    }

}