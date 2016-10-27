/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import jade.core.Agent;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EnvironmentAgentInfoTest {

    @Test
    public void shouldGetCorrectToStringWithArguments() {
        EnvironmentAgentInfo actualAgentInfo = new EnvironmentAgentInfo("agent", Agent.class, Arrays.asList("arg1", "arg2"));
        assertThat(actualAgentInfo.toString(), is("agent:jade.core.Agent(arg1,arg2)"));
    }

    @Test
    public void shouldGetCorrectToStringWithoutArguments() {
        EnvironmentAgentInfo actualAgentInfo = new EnvironmentAgentInfo("agent", Agent.class, null);
        assertThat(actualAgentInfo.toString(), is("agent:jade.core.Agent"));
    }

}