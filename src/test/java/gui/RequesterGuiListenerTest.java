/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.gui.GuiEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.event.WindowEvent;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class RequesterGuiListenerTest {

    private RequesterGuiListener requesterGuiListener;
    private RequesterGui requesterGuiMock;
    private ArgumentCaptor<GuiEvent> guiEventArgumentCaptor;

    @Before
    public void setUp() {
        guiEventArgumentCaptor = ArgumentCaptor.forClass(GuiEvent.class);
        requesterGuiMock = mock(RequesterGui.class);
        requesterGuiListener = new RequesterGuiListener(requesterGuiMock);
    }

    @Test
    public void shouldInvokeOnGuiEvent() {
        Object sourceMock = mock(Object.class);
        GuiEvent expectedGuiEvent = new GuiEvent(sourceMock, RequesterGuiEvent.CLOSE_WINDOW.getInt());

        WindowEvent windowEventMock = mock(WindowEvent.class);
        doReturn(sourceMock).when(windowEventMock).getSource();

        requesterGuiListener.windowClosing(windowEventMock);

        verify(requesterGuiMock).onGuiEvent(guiEventArgumentCaptor.capture());
        assertReflectionEquals(expectedGuiEvent, guiEventArgumentCaptor.getValue());
    }

}