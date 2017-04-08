/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import java.util.Arrays;
import java.util.List;

public enum EmotionalStateAgentEvent {

    CLOSE_WINDOW;

    private static List<EmotionalStateAgentEvent> events = Arrays.asList(EmotionalStateAgentEvent.values());

    public static EmotionalStateAgentEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(EmotionalStateAgentEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}