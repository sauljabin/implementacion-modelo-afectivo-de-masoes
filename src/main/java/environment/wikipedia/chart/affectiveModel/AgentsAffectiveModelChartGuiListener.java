/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.affectiveModel;

import gui.WindowsEventsAdapter;

import java.awt.event.WindowEvent;

public class AgentsAffectiveModelChartGuiListener extends WindowsEventsAdapter {

    private AgentsAffectiveModelChartGui chartGui;

    public AgentsAffectiveModelChartGuiListener(AgentsAffectiveModelChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        chartGui.stop();
    }

}
