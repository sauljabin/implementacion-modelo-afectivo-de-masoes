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

public class AffectiveModelChartGuiListenerTest {

    private AffectiveModelChartGuiListener affectiveModelChartGuiListener;
    private AffectiveModelChartGui affectiveModelChartGuiMock;
    private ArgumentCaptor<GuiEvent> guiEventArgumentCaptor;
    private AffectiveModelChartGuiAgent affectiveModelChartGuiAgentMock;

    @Before
    public void setUp() {
        guiEventArgumentCaptor = ArgumentCaptor.forClass(GuiEvent.class);
        affectiveModelChartGuiMock = mock(AffectiveModelChartGui.class);
        affectiveModelChartGuiAgentMock = mock(AffectiveModelChartGuiAgent.class);
        affectiveModelChartGuiListener = new AffectiveModelChartGuiListener(affectiveModelChartGuiAgentMock, affectiveModelChartGuiMock);
    }

    @Test
    public void shouldSetUpWindowsListenerListener() {
        verify(affectiveModelChartGuiMock).addWindowListener(affectiveModelChartGuiListener);
    }

    @Test
    public void shouldInvokeOnGuiEvent() {
        GuiEvent expectedGuiEvent = new GuiEvent(affectiveModelChartGuiMock, RequesterGuiEvent.CLOSE_WINDOW.getInt());

        WindowEvent windowEventMock = mock(WindowEvent.class);
        doReturn(affectiveModelChartGuiMock).when(windowEventMock).getSource();

        affectiveModelChartGuiListener.windowClosing(windowEventMock);

        verify(affectiveModelChartGuiAgentMock).postGuiEvent(guiEventArgumentCaptor.capture());
        assertReflectionEquals(expectedGuiEvent, guiEventArgumentCaptor.getValue());
    }

}