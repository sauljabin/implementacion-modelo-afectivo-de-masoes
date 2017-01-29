/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.view;

import gui.agent.RequesterGuiAgent;
import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;

public class RequesterGui extends JFrame {

    private static final String ONTOLOGY_REQUESTER_GUI = "Requester GUI";
    private RequesterGuiAgent requesterGuiAgent;

    public RequesterGui(RequesterGuiAgent requesterGuiAgent) {
        this.requesterGuiAgent = requesterGuiAgent;
    }

    public static void main(String[] args) {
        RequesterGui requesterGui = new RequesterGui(null);
        requesterGui.setUp();
        requesterGui.showGui();
    }

    public void setUp() {
        setTitle(ONTOLOGY_REQUESTER_GUI);
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        addWindowListener(new RequesterGuiListener(this));
        setLocationRelativeTo(this);
    }

    public void showGui() {
        setVisible(true);
    }

    public void closeGui() {
        setVisible(false);
        dispose();
    }

    public void onGuiEvent(GuiEvent guiEvent) {
        requesterGuiAgent.postGuiEvent(guiEvent);
    }

}
