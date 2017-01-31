/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import jade.core.Agent;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AgentParameterTest {

    @Test
    public void shouldGetCorrectFormatWithArguments() {
        AgentParameter formatter = new AgentParameter("agent", Agent.class, Arrays.asList("arg1", "arg2"));
        assertThat(formatter.toJadeParameter(), is("agent:jade.core.Agent(arg1,arg2)"));
    }

    @Test
    public void shouldGetCorrectFormatWithEmptyArguments() {
        AgentParameter formatter = new AgentParameter("agent", Agent.class, Arrays.asList());
        assertThat(formatter.toJadeParameter(), is("agent:jade.core.Agent"));
    }

    @Test
    public void shouldGetCorrectFormatWithoutArguments() {
        AgentParameter formatter = new AgentParameter("agent", Agent.class);
        assertThat(formatter.toJadeParameter(), is("agent:jade.core.Agent"));
    }

}