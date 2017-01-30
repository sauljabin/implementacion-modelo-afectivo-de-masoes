/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.gui.GuiEvent;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class RequesterGuiTest {

    private RequesterGui requesterGui;
    private RequesterGui requesterGuiSpy;
    private RequesterGui requesterGuiMock;
    private RequesterGuiAgent requesterGuiAgentMock;

    @Before
    public void setUp() {
        requesterGuiAgentMock = mock(RequesterGuiAgent.class);
        requesterGui = new RequesterGui(requesterGuiAgentMock);
        requesterGuiSpy = spy(requesterGui);
        requesterGuiMock = mock(RequesterGui.class);
        doNothing().when(requesterGuiSpy).setVisible(anyBoolean());
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
        verify(requesterGuiSpy).setVisible(true);
    }

}
