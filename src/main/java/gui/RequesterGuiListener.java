/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.gui.GuiEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RequesterGuiListener extends WindowAdapter {

    private RequesterGui requesterGui;

    public RequesterGuiListener(RequesterGui requesterGui) {
        this.requesterGui = requesterGui;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(e.getSource(), RequesterGuiEvent.CLOSE_WINDOW.getInt());
        requesterGui.onGuiEvent(guiEvent);
    }

}
