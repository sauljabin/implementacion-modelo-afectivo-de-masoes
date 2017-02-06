/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import java.util.Arrays;
import java.util.List;

public enum RequesterGuiAction {

    CLOSE_WINDOW, SEND_MESSAGE;

    private static List<RequesterGuiAction> events = Arrays.asList(RequesterGuiAction.values());

    public static RequesterGuiAction fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(RequesterGuiAction event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}
