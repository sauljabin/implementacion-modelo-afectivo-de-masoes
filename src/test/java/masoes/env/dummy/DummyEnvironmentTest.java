/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.env.EnvironmentAgentInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DummyEnvironmentTest {

    private DummyEnvironment dummyEnvironment;
    private List<EnvironmentAgentInfo> expectedAgentsInfo;

    @Before
    public void setUp() {
        expectedAgentsInfo = new ArrayList<>();
        expectedAgentsInfo.add(new EnvironmentAgentInfo("dummy", DummyEmotionalAgent.class));
        dummyEnvironment = new DummyEnvironment();
    }

    @Test
    public void shouldReturnAllAgentInfo() {
        assertReflectionEquals(expectedAgentsInfo, dummyEnvironment.getEnvironmentAgentInfoList());
    }

}