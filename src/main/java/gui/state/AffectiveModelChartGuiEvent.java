/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import java.util.Arrays;
import java.util.List;

public enum AffectiveModelChartGuiEvent {

    CLOSE_WINDOW;

    private static List<AffectiveModelChartGuiEvent> events = Arrays.asList(AffectiveModelChartGuiEvent.values());

    public static AffectiveModelChartGuiEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(AffectiveModelChartGuiEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}