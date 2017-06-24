/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import java.util.Arrays;
import java.util.List;

public enum ConfiguratorViewEvent {

    CLOSE_WINDOW,
    ADD_STIMULUS,
    DELETE_STIMULUS,
    EDIT_STIMULUS;

    private static List<ConfiguratorViewEvent> events = Arrays.asList(ConfiguratorViewEvent.values());

    public static ConfiguratorViewEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(ConfiguratorViewEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}
