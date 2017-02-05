/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.gui.GuiEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RequesterGuiListener extends WindowAdapter implements ActionListener {

    private RequesterGui requesterGui;
    private RequesterGuiAgent requesterGuiAgent;

    public RequesterGuiListener(RequesterGui requesterGui, RequesterGuiAgent requesterGuiAgent) {
        this.requesterGui = requesterGui;
        this.requesterGuiAgent = requesterGuiAgent;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(e.getSource(), RequesterGuiEventType.CLOSE_WINDOW.getInt());
        requesterGuiAgent.postGuiEvent(guiEvent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
