/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.agentstate;

import agent.AgentException;
import jade.JadeSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static test.ReflectionTestUtils.setFieldValue;

public class AgentStateGuiAgentTest {

    private static final String AGENT_NAME = "agentName";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private AgentStateGui agentStateGuiMock;
    private AgentStateGuiAgent agentStateGuiAgent;
    private JadeSettings jadeSettings;

    @Before
    public void setUp() throws Exception {
        agentStateGuiMock = mock(AgentStateGui.class);

        agentStateGuiAgent = new AgentStateGuiAgent();
        setFieldValue(agentStateGuiAgent, "agentStateGui", agentStateGuiMock);
        setFieldValue(agentStateGuiAgent, "myName", AGENT_NAME);
        jadeSettings = JadeSettings.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(jadeSettings, "INSTANCE", null);
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        agentStateGuiAgent.takeDown();
        verify(agentStateGuiMock).closeGui();
    }

    @Test
    public void shouldThrowExceptionWhenJadeGuiOptionIsDisable() {
        expectedException.expect(AgentException.class);
        expectedException.expectMessage(AGENT_NAME + ": gui option is disabled");
        jadeSettings.set(JadeSettings.GUI, Boolean.toString(false));
        agentStateGuiAgent.setup();
    }

}