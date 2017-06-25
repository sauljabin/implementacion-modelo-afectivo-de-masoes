/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import java.util.Arrays;
import java.util.List;

public enum ConfiguratorGuiEvent {

    CLOSE_WINDOW,
    ADD_STIMULUS,
    DELETE_STIMULUS,
    EDIT_STIMULUS,
    ADD_AGENT,
    DELETE_AGENT,
    EDIT_AGENT,
    PLAY,
    PAUSE,
    REFRESH,
    SHOW_AGENT_STATE;

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
