/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.chart.behaviourmodification;

import org.jfree.data.xy.XYSeries;
import util.Colors;

import java.awt.*;

public class SeriesBehaviourContainer {

    private String name;
    private int sequence;
    private Color color;
    private XYSeries xySeries;

    public SeriesBehaviourContainer(String name, int sequence) {
        this.name = name;
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        if (color == null) {
            color = Colors.getColor(sequence);
        }
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SeriesBehaviourContainer)) {
            return false;
        }
        return getName().equals(((SeriesBehaviourContainer) obj).getName());
    }

    public boolean sameName(String name) {
        return getName().equals(name);
    }

    public int getSequence() {
        return sequence;
    }

    public XYSeries getSeries() {
        if (xySeries == null) {
            xySeries = new XYSeries(name);
        }
        return xySeries;
    }

}
