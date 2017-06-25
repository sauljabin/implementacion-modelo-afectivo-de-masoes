/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.emotionModification;

import gui.WindowsEventsAdapter;

import java.awt.event.WindowEvent;

public class AgentsEmotionModificationChartGuiListener extends WindowsEventsAdapter {

    private AgentsEmotionModificationChartGui chartGui;

    public AgentsEmotionModificationChartGuiListener(AgentsEmotionModificationChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.chartGui.stop();
    }

}