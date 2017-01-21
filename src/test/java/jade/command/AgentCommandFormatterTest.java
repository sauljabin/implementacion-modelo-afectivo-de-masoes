/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.command;

import jade.core.Agent;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AgentCommandFormatterTest {

    @Test
    public void shouldGetCorrectFormatWithArguments() {
        AgentCommandFormatter formatter = new AgentCommandFormatter("agent", Agent.class, Arrays.asList("arg1", "arg2"));
        assertThat(formatter.format(), is("agent:jade.core.Agent(arg1,arg2)"));
    }

    @Test
    public void shouldGetCorrectFormatWithoutArguments() {
        AgentCommandFormatter formatter = new AgentCommandFormatter("agent", Agent.class);
        assertThat(formatter.format(), is("agent:jade.core.Agent"));
    }

}