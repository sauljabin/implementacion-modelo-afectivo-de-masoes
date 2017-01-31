/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import jade.core.Agent;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class EnvironmentTest {

    private static final String NAME = "name";
    private Environment environment;

    @Before
    public void setUp() {
        environment = new Environment();
    }

    @Test
    public void shouldGetNameWhenSetAndNotSet() {
        assertThat(environment.getEnvironmentName(), is("Environment"));
        environment.setEnvironmentName(NAME);
        assertThat(environment.getEnvironmentName(), is(NAME));
    }

    @Test
    public void shouldAddAndRemoveAgentCommand() {
        assertThat(environment.getAgentParameters(), is(empty()));
        AgentParameter agentParameterMock = mock(AgentParameter.class);
        environment.add(agentParameterMock);
        assertThat(environment.getAgentParameters(), hasSize(1));
        assertThat(environment.getAgentParameters(), hasItem(agentParameterMock));
        environment.remove(agentParameterMock);
        assertThat(environment.getAgentParameters(), hasSize(0));
        assertThat(environment.getAgentParameters(), not(hasItem(agentParameterMock)));
    }

    @Test
    public void shouldConvertOneAgentToCorrectAgentsParameterFormat() {
        environment.add(new AgentParameter("agent", Agent.class));
        assertThat(environment.toJadeParameter(), is("agent:jade.core.Agent"));
        assertThat(environment.toJadeParameterList(), hasItem("agent:jade.core.Agent"));
    }

    @Test
    public void shouldConvertTwoAgentToCorrectAgentsParameterFormat() {
        environment.add(new AgentParameter("agent", Agent.class));
        environment.add(new AgentParameter("agent2", Agent.class));
        assertThat(environment.toJadeParameter(), is("agent:jade.core.Agent;agent2:jade.core.Agent"));
        assertThat(environment.toJadeParameterList(), hasItems("agent:jade.core.Agent", "agent2:jade.core.Agent"));
    }

    @Test
    public void shouldConvertOneAgentToCorrectAgentsParameterFormatWithArguments() {
        environment.add(new AgentParameter("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        assertThat(environment.toJadeParameter(), is("agent:jade.core.Agent(arg1,arg2)"));
        assertThat(environment.toJadeParameterList(), hasItem("agent:jade.core.Agent(arg1,arg2)"));
    }

    @Test
    public void shouldConvertOneAgentToCorrectAgentsParameterFormatWithEmptyArguments() {
        environment.add(new AgentParameter("agent", Agent.class, Arrays.asList()));
        assertThat(environment.toJadeParameter(), is("agent:jade.core.Agent"));
        assertThat(environment.toJadeParameterList(), hasItem("agent:jade.core.Agent"));
    }

    @Test
    public void shouldConvertTwoAgentToCorrectAgentsParameterFormatWithArguments() {
        environment.add(new AgentParameter("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        environment.add(new AgentParameter("agent2", Agent.class, Arrays.asList("arg1", "arg2")));
        assertThat(environment.toJadeParameter(), is("agent:jade.core.Agent(arg1,arg2);agent2:jade.core.Agent(arg1,arg2)"));
        assertThat(environment.toJadeParameterList(), hasItems("agent:jade.core.Agent(arg1,arg2)", "agent2:jade.core.Agent(arg1,arg2)"));
    }

    @Test
    public void shouldReturnEmptyWhenNotHaveAgentParameter() {
        assertThat(environment.toJadeParameter(), isEmptyString());
        assertThat(environment.toJadeParameterList(), is(empty()));
    }

}