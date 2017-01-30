/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import java.util.Arrays;
import java.util.List;

public enum RequesterGuiEventType {

    CLOSE_WINDOW;

    private static List<RequesterGuiEventType> events = Arrays.asList(RequesterGuiEventType.values());

    public static RequesterGuiEventType fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(RequesterGuiEventType event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}
