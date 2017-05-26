/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AffectiveModelChartGuiEventTest {

    @Test
    public void shouldGetCorrectIntegerValue() {
        AffectiveModelChartGuiEvent[] events = AffectiveModelChartGuiEvent.values();
        Arrays.stream(events).forEach(
                event -> assertThat(event.getInt(), is(AffectiveModelChartGuiEvent.toInt(event)))
        );
    }

    @Test
    public void shouldConvertFromIntegerValue() {
        AffectiveModelChartGuiEvent[] events = AffectiveModelChartGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertThat(AffectiveModelChartGuiEvent.fromInt(i), is(events[i]));
            assertThat(AffectiveModelChartGuiEvent.toInt(events[i]), is(i));
        }
    }

    @Test
    public void shouldReturnEvenEqualsWhenInvokeIntEqual() {
        AffectiveModelChartGuiEvent[] events = AffectiveModelChartGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertTrue(events[i].equals(i));
        }
    }

}