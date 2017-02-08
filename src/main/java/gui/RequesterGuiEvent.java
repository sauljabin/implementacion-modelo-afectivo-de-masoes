/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import java.util.Arrays;
import java.util.List;

public enum RequesterGuiEvent {

    CLOSE_WINDOW, SEND_MESSAGE, CHANGE_ACTION;

    private static List<RequesterGuiEvent> events = Arrays.asList(RequesterGuiEvent.values());

    public static RequesterGuiEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(RequesterGuiEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}
