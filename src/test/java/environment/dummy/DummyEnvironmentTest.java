/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import environment.base.AgentCommand;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DummyEnvironmentTest {

    private DummyEnvironment dummyEnvironment;
    private List<AgentCommand> expectedAgentsInfo;

    @Before
    public void setUp() {
        expectedAgentsInfo = new ArrayList<>();
        expectedAgentsInfo.add(new AgentCommand("dummy", DummyEmotionalAgent.class));
        dummyEnvironment = new DummyEnvironment();
    }

    @Test
    public void shouldReturnAllAgentInfo() {
        assertReflectionEquals(expectedAgentsInfo, dummyEnvironment.getAgentCommands());
    }

}