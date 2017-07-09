/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

import java.util.Arrays;
import java.util.List;

public enum SocialEmotionGuiEvent {

    CLOSE_WINDOW;

    private static List<SocialEmotionGuiEvent> events = Arrays.asList(SocialEmotionGuiEvent.values());

    public static SocialEmotionGuiEvent fromInt(int i) {
        return events.get(i);
    }

    public static int toInt(SocialEmotionGuiEvent event) {
        return events.indexOf(event);
    }

    public int getInt() {
        return toInt(this);
    }

    public boolean equals(int i) {
        return i == toInt(this);
    }

}