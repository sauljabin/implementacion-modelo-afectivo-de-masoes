/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import jade.gui.GuiEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RequesterGuiListener extends WindowAdapter implements ActionListener {

    private RequesterGui requesterGui;
    private RequesterGuiAgent requesterGuiAgent;

    public RequesterGuiListener(RequesterGuiAgent requesterGuiAgent, RequesterGui requesterGui) {
        this.requesterGui = requesterGui;
        this.requesterGuiAgent = requesterGuiAgent;
        setUpGui();
    }

    private void setUpGui() {
        requesterGui.addWindowListener(this);
        requesterGui.addActionListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(requesterGui, RequesterGuiEvent.CLOSE_WINDOW.getInt());
        requesterGuiAgent.postGuiEvent(guiEvent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiEvent guiEvent = new GuiEvent(requesterGui, RequesterGuiEvent.valueOf(e.getActionCommand()).getInt());
        requesterGuiAgent.postGuiEvent(guiEvent);
    }

}
