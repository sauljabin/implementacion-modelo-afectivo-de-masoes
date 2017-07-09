/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.agentstate;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AgentStateGuiEventTest {

    @Test
    public void shouldGetCorrectIntegerValue() {
        AgentStateGuiEvent[] events = AgentStateGuiEvent.values();
        Arrays.stream(events).forEach(
                event -> assertThat(event.getInt(), is(AgentStateGuiEvent.toInt(event)))
        );
    }

    @Test
    public void shouldConvertFromIntegerValue() {
        AgentStateGuiEvent[] events = AgentStateGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertThat(AgentStateGuiEvent.fromInt(i), is(events[i]));
            assertThat(AgentStateGuiEvent.toInt(events[i]), is(i));
        }
    }

    @Test
    public void shouldReturnEvenEqualsWhenInvokeIntEqual() {
        AgentStateGuiEvent[] events = AgentStateGuiEvent.values();
        for (int i = 0; i < events.length; i++) {
            assertTrue(events[i].equals(i));
        }
    }

}