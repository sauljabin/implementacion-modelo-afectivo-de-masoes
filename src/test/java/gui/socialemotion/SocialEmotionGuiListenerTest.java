/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

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

public class SocialEmotionGuiListenerTest {

    private SocialEmotionGuiListener socialEmotionGuiListener;
    private SocialEmotionGui socialEmotionGuiMock;
    private ArgumentCaptor<GuiEvent> guiEventArgumentCaptor;
    private SocialEmotionGuiAgent socialEmotionGuiAgentMock;

    @Before
    public void setUp() {
        guiEventArgumentCaptor = ArgumentCaptor.forClass(GuiEvent.class);
        socialEmotionGuiMock = mock(SocialEmotionGui.class);
        socialEmotionGuiAgentMock = mock(SocialEmotionGuiAgent.class);
        socialEmotionGuiListener = new SocialEmotionGuiListener(socialEmotionGuiAgentMock, socialEmotionGuiMock);
    }

    @Test
    public void shouldSetUpWindowsListenerListener() {
        verify(socialEmotionGuiMock).addWindowListener(socialEmotionGuiListener);
    }

    @Test
    public void shouldInvokeOnGuiEvent() {
        GuiEvent expectedGuiEvent = new GuiEvent(socialEmotionGuiMock, RequesterGuiEvent.CLOSE_WINDOW.getInt());

        WindowEvent windowEventMock = mock(WindowEvent.class);
        doReturn(socialEmotionGuiMock).when(windowEventMock).getSource();

        socialEmotionGuiListener.windowClosing(windowEventMock);

        verify(socialEmotionGuiAgentMock).postGuiEvent(guiEventArgumentCaptor.capture());
        assertReflectionEquals(expectedGuiEvent, guiEventArgumentCaptor.getValue());
    }

}