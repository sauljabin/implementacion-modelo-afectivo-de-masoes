/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator;

import java.util.Arrays;
import java.util.List;

public enum ConfiguratorGuiEvent {

    CLOSE_WINDOW,
    ADD_AGENT,
    DELETE_AGENT,
    EDIT_AGENT,
    PLAY,
    PAUSE,
    REFRESH,
    SHOW_AGENT_STATE,
    HIDE_CENTRAL_EMOTION_CHART,
    SHOW_CENTRAL_EMOTION_CHART,
    HIDE_MAXIMUM_DISTANCE_CHART,
    SHOW_MAXIMUM_DISTANCE_CHART,
    HIDE_EMOTIONAL_DISPERSION_CHART,
    SHOW_EMOTIONAL_DISPERSION_CHART,
    HIDE_BEHAVIOUR_MODIFICATION_CHART,
    SHOW_BEHAVIOUR_MODIFICATION_CHART,
    HIDE_EMOTION_MODIFICATION_CHART,
    SHOW_EMOTION_MODIFICATION_CHART,
    HIDE_EMOTIONAL_STATE_CHART,
    SHOW_EMOTIONAL_STATE_CHART,
    SHOW_AGENT_TYPE_DEFINITION_GUI,
    SHOW_STIMULUS_DEFINITION_GUI;

    private static List<ConfiguratorGuiEvent> events = Arrays.asList(ConfiguratorGuiEvent.values());

    public static ConfiguratorGuiEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(ConfiguratorGuiEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}
