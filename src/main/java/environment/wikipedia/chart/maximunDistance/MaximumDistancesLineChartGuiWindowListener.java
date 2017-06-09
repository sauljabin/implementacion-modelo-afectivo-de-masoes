/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.maximunDistance;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MaximumDistancesLineChartGuiWindowListener extends WindowAdapter {

    private MaximumDistancesLineChartGui chartGui;

    public MaximumDistancesLineChartGuiWindowListener(MaximumDistancesLineChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.chartGui.stop();
    }

}
