/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import jade.core.Agent;
import masoes.colective.ColectiveKnowledgeBaseAgent;
import masoes.colective.NotifierAgent;
import org.junit.Test;
import settings.SettingsAgent;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class EnvironmentTest {

    @Test
    public void shouldConvertOneAgentToCorrectAgentsParameterFormat() {
        Environment environment = createEnvironment(Arrays.asList(new AgentParameter("agent", Agent.class)));
        assertThat(environment.toJadeParameter(), containsString("agent:jade.core.Agent"));
        testBasicAgents(environment);
    }

    @Test
    public void shouldConvertTwoAgentToCorrectAgentsParameterFormat() {
        Environment environment = createEnvironment(Arrays.asList(
                new AgentParameter("agent", Agent.class),
                new AgentParameter("agent2", Agent.class)
        ));
        assertThat(environment.toJadeParameter(), containsString("agent:jade.core.Agent;agent2:jade.core.Agent"));
        testBasicAgents(environment);
    }

    @Test
    public void shouldConvertOneAgentToCorrectAgentsParameterFormatWithArguments() {
        Environment environment = createEnvironment(Arrays.asList(new AgentParameter("agent", Agent.class, Arrays.asList("arg1", "arg2"))));
        assertThat(environment.toJadeParameter(), containsString("agent:jade.core.Agent(arg1,arg2)"));
        testBasicAgents(environment);
    }

    @Test
    public void shouldConvertOneAgentToCorrectAgentsParameterFormatWithEmptyArguments() {
        Environment environment = createEnvironment(Arrays.asList(new AgentParameter("agent", Agent.class, Arrays.asList())));
        assertThat(environment.toJadeParameter(), containsString("agent:jade.core.Agent"));
        testBasicAgents(environment);
    }

    @Test
    public void shouldConvertTwoAgentToCorrectAgentsParameterFormatWithArguments() {
        Environment environment = createEnvironment(Arrays.asList(
                new AgentParameter("agent", Agent.class, Arrays.asList("arg1", "arg2")),
                new AgentParameter("agent2", Agent.class, Arrays.asList("arg1", "arg2"))
        ));
        assertThat(environment.toJadeParameter(), containsString("agent:jade.core.Agent(arg1,arg2);agent2:jade.core.Agent(arg1,arg2)"));
        testBasicAgents(environment);
    }

    @Test
    public void shouldReturnNotifierAndSettingsWhenNotHaveAgentParameter() {
        Environment environment = createEnvironment(Arrays.asList());
        testBasicAgents(environment);
    }

    @Test
    public void shouldReturnNotifierAndSettingsWhenNullAgentParameter() {
        Environment environment = createEnvironment(null);
        testBasicAgents(environment);
    }

    private void testBasicAgents(Environment environment) {
        assertThat(environment.toJadeParameter(), containsString("notifier:" + NotifierAgent.class.getName()));
        assertThat(environment.toJadeParameter(), containsString("settings:" + SettingsAgent.class.getName()));
        assertThat(environment.toJadeParameter(), containsString("persistence:" + ColectiveKnowledgeBaseAgent.class.getName()));
    }

    private Environment createEnvironment(List<AgentParameter> agentParameters) {
        return new Environment() {
            @Override
            public List<AgentParameter> getAgentParameters() {
                return agentParameters;
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

}