/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import java.util.Arrays;
import java.util.List;

public enum ConfiguratorAgentEvent {

    CLOSE_WINDOW,
    UPDATE_SATISFACTION_PARAMETER,
    UPDATE_ACTIVATION_PARAMETER,
    START,
    CLEAN,
    ADD_AGENT,
    REMOVE_AGENTS,
    UPDATE_SATISFACTION_TO_ADD,
    UPDATE_ACTIVATION_TO_ADD,
    SHOW_EMOTIONAL_STATE_GUI,
    SAVE,
    SELECT_ALL_AGENTS_TO_ADD,
    DESELECT_ALL_AGENTS_TO_ADD,
    UPDATE_INTERVAL_BETWEEN_EVENTS,
    UPDATE_ITERATIONS,
    UPDATE_RANDOM_CHECKBOX;

    private static List<ConfiguratorAgentEvent> events = Arrays.asList(ConfiguratorAgentEvent.values());

    public static ConfiguratorAgentEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(ConfiguratorAgentEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}
