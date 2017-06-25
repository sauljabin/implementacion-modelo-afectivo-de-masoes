/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.emotionalState;

import gui.WindowsEventsAdapter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AgentsEmotionalStateChartGuiListener extends WindowsEventsAdapter {

    private AgentsEmotionalStateChartGui chartGui;

    public AgentsEmotionalStateChartGuiListener(AgentsEmotionalStateChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.chartGui.stop();
    }

}
