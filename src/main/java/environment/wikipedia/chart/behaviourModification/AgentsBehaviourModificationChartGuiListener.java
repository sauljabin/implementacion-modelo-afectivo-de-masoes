/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.behaviourModification;

import gui.WindowsEventsAdapter;

import java.awt.event.WindowEvent;

public class AgentsBehaviourModificationChartGuiListener extends WindowsEventsAdapter {

    private AgentsBehaviourModificationChartGui chartGui;

    public AgentsBehaviourModificationChartGuiListener(AgentsBehaviourModificationChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.chartGui.stop();
    }

}
