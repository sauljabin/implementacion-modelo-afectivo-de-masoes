/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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
    public void shouldAddAgentCommand() {
        assertThat(environment.getAgentCommands(), is(empty()));
        AgentCommand agentCommandMock = mock(AgentCommand.class);
        environment.add(agentCommandMock);
        assertThat(environment.getAgentCommands(), hasSize(1));
        assertThat(environment.getAgentCommands(), hasItem(agentCommandMock));
    }

}