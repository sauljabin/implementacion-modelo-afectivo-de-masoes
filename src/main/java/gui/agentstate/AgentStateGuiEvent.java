/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.agentstate;

import java.util.Arrays;
import java.util.List;

public enum AgentStateGuiEvent {

    CLOSE_WINDOW;

    private static List<AgentStateGuiEvent> events = Arrays.asList(AgentStateGuiEvent.values());

    public static AgentStateGuiEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(AgentStateGuiEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}