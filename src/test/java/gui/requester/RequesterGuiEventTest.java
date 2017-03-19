/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RequesterGuiEventTest {

    @Test
    public void shouldGetCorrectIntegerValue() {
        RequesterGuiEvent[] events = RequesterGuiEvent.values();
        Arrays.stream(events).forEach(
                event -> assertThat(event.getInt(), is(RequesterGuiEvent.toInt(event)))
        );
    }

    @Test
    public void shouldConvertFromIntegerValue() {
        RequesterGuiEvent[] events = RequesterGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertThat(RequesterGuiEvent.fromInt(i), is(events[i]));
            assertThat(RequesterGuiEvent.toInt(events[i]), is(i));
        }
    }

    @Test
    public void shouldReturnEvenEqualsWhenInvokeIntEqual() {
        RequesterGuiEvent[] events = RequesterGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertTrue(events[i].equals(i));
        }
    }

}