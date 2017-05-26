/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import jade.gui.GuiEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AffectiveModelChartGuiListener extends WindowAdapter {

    private AffectiveModelChartGui affectiveModelChartGui;
    private AffectiveModelChartGuiAgent affectiveModelChartGuiAgent;

    public AffectiveModelChartGuiListener(AffectiveModelChartGuiAgent affectiveModelChartGuiAgent, AffectiveModelChartGui affectiveModelChartGui) {
        this.affectiveModelChartGui = affectiveModelChartGui;
        this.affectiveModelChartGuiAgent = affectiveModelChartGuiAgent;
        setUpGui();
    }

    private void setUpGui() {
        affectiveModelChartGui.addWindowListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(affectiveModelChartGui, AffectiveModelChartGuiEvent.CLOSE_WINDOW.getInt());
        affectiveModelChartGuiAgent.postGuiEvent(guiEvent);
    }

}
