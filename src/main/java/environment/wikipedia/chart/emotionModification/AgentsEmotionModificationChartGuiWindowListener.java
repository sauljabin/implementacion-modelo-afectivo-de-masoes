/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.emotionModification;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AgentsEmotionModificationChartGuiWindowListener extends WindowAdapter {

    private AgentsEmotionModificationChartGui chartGui;

    public AgentsEmotionModificationChartGuiWindowListener(AgentsEmotionModificationChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.chartGui.stop();
    }

}