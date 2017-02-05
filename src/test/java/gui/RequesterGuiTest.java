/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class RequesterGuiTest {

    private RequesterGui requesterGui;
    private RequesterGui requesterGuiSpy;

    @Before
    public void setUp() {
        requesterGui = new RequesterGui();
        requesterGuiSpy = spy(requesterGui);
        doNothing().when(requesterGuiSpy).setVisible(anyBoolean());
        doNothing().when(requesterGuiSpy).dispose();
    }

    @Test
    public void shouldCloseWindow() {
        doCallRealMethod().when(requesterGuiSpy).closeGui();
        requesterGuiSpy.closeGui();
        verify(requesterGuiSpy).setVisible(false);
        verify(requesterGuiSpy).dispose();
    }

    @Test
    public void shouldSetupViewConfiguration() {
        requesterGuiSpy.setUp();
        verify(requesterGuiSpy).setTitle("Requester GUI");
        verify(requesterGuiSpy).setSize(1024, 768);
        verify(requesterGuiSpy).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        verify(requesterGuiSpy).setLayout(any(BorderLayout.class));
        verify(requesterGuiSpy).setLocationRelativeTo(requesterGuiSpy);
        verify(requesterGuiSpy).setVisible(true);
    }

}
