/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.emotionModification;

import org.jfree.data.xy.XYSeries;
import util.Colors;

import java.awt.*;

public class SeriesEmotionContainer {

    private String agentName;
    private int sequence;
    private Color color;
    private XYSeries xySeries;

    public SeriesEmotionContainer(String agentName, int sequence) {
        this.agentName = agentName;
        this.sequence = sequence;
    }

    public String getAgentName() {
        return agentName;
    }

    public Color getColor() {
        if (color == null) {
            color = Colors.getColor(sequence);
        }
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SeriesEmotionContainer)) {
            return false;
        }
        return getAgentName().equals(((SeriesEmotionContainer) obj).getAgentName());
    }

    public boolean sameName(String name) {
        return getAgentName().equals(name);
    }

    public int getSequence() {
        return sequence;
    }

    public XYSeries getSeries() {
        if (xySeries == null) {
            xySeries = new XYSeries(agentName);
        }
        return xySeries;
    }

}
