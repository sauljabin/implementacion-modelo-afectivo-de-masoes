/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import environment.AgentParameter;
import gui.agentstate.AgentStateGuiAgent;
import gui.requester.RequesterGuiAgent;
import gui.socialemotion.SocialEmotionGuiAgent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DummyEnvironmentTest {

    private DummyEnvironment dummyEnvironment;
    private List<AgentParameter> expectedAgentsInfo;

    @Before
    public void setUp() {
        expectedAgentsInfo = new ArrayList<>();
        expectedAgentsInfo.add(new AgentParameter("dummy", DummyEmotionalAgent.class));
        expectedAgentsInfo.add(new AgentParameter("dummyGUI", AgentStateGuiAgent.class, Arrays.asList("dummy")));
        expectedAgentsInfo.add(new AgentParameter("requester", RequesterGuiAgent.class));
        expectedAgentsInfo.add(new AgentParameter("socialEmotionGUI", SocialEmotionGuiAgent.class));
        dummyEnvironment = new DummyEnvironment();
    }

    @Test
    public void shouldReturnAllAgentInfo() {
        assertReflectionEquals(expectedAgentsInfo, dummyEnvironment.getAgentParameters());
    }

    @Test
    public void shouldReturnName() {
        assertThat(dummyEnvironment.getName(), is("dummy"));
    }

}