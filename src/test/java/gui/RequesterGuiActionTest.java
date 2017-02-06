/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RequesterGuiActionTest {

    @Test
    public void shouldGetCorrectIntegerValue() {
        RequesterGuiAction[] events = RequesterGuiAction.values();
        Arrays.stream(events).forEach(
                event -> assertThat(event.getInt(), is(RequesterGuiAction.toInt(event)))
        );
    }

    @Test
    public void shouldConvertFromIntegerValue() throws Exception {
        RequesterGuiAction[] events = RequesterGuiAction.values();
        for (int i = 0; i < events.length; i++) {
            assertThat(RequesterGuiAction.fromInt(i), is(events[i]));
            assertThat(RequesterGuiAction.toInt(events[i]), is(i));
        }
    }

    @Test
    public void shouldReturnEvenEqualsWhenInvokeIntEqueal() throws Exception {
        RequesterGuiAction[] events = RequesterGuiAction.values();
        for (int i = 0; i < events.length; i++) {
            assertTrue(events[i].equals(i));
        }
    }

}