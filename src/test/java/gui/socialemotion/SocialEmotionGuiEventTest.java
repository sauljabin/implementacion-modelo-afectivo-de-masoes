/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SocialEmotionGuiEventTest {

    @Test
    public void shouldGetCorrectIntegerValue() {
        SocialEmotionGuiEvent[] events = SocialEmotionGuiEvent.values();
        Arrays.stream(events).forEach(
                event -> assertThat(event.getInt(), is(SocialEmotionGuiEvent.toInt(event)))
        );
    }

    @Test
    public void shouldConvertFromIntegerValue() {
        SocialEmotionGuiEvent[] events = SocialEmotionGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertThat(SocialEmotionGuiEvent.fromInt(i), is(events[i]));
            assertThat(SocialEmotionGuiEvent.toInt(events[i]), is(i));
        }
    }

    @Test
    public void shouldReturnEvenEqualsWhenInvokeIntEqual() {
        SocialEmotionGuiEvent[] events = SocialEmotionGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertTrue(events[i].equals(i));
        }
    }

}