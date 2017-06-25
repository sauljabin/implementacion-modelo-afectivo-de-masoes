/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.maximunDistance;

import gui.WindowsEventsAdapter;

import java.awt.event.WindowEvent;

public class MaximumDistancesLineChartGuiListener extends WindowsEventsAdapter {

    private MaximumDistancesLineChartGui chartGui;

    public MaximumDistancesLineChartGuiListener(MaximumDistancesLineChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.chartGui.stop();
    }

}
