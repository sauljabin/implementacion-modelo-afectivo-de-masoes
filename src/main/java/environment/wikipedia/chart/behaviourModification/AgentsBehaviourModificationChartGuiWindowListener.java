/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.behaviourModification;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AgentsBehaviourModificationChartGuiWindowListener extends WindowAdapter {

    private AgentsBehaviourModificationChartGui chartGui;

    public AgentsBehaviourModificationChartGuiWindowListener(AgentsBehaviourModificationChartGui chartGui) {
        this.chartGui = chartGui;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.chartGui.stop();
    }

}
