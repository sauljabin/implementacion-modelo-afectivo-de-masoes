/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

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

public class AffectiveModelChartGuiAgentTest {

    private static final String AGENT_NAME = "agentName";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private AffectiveModelChartGui affectiveModelChartGuiMock;
    private AffectiveModelChartGuiAgent affectiveModelChartGuiAgent;
    private JadeSettings jadeSettings;

    @Before
    public void setUp() throws Exception {
        affectiveModelChartGuiMock = mock(AffectiveModelChartGui.class);

        affectiveModelChartGuiAgent = new AffectiveModelChartGuiAgent();
        setFieldValue(affectiveModelChartGuiAgent, "affectiveModelChartGui", affectiveModelChartGuiMock);
        setFieldValue(affectiveModelChartGuiAgent, "myName", AGENT_NAME);
        jadeSettings = JadeSettings.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(jadeSettings, "INSTANCE", null);
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        affectiveModelChartGuiAgent.takeDown();
        verify(affectiveModelChartGuiMock).closeGui();
    }

    @Test
    public void shouldThrowExceptionWhenJadeGuiOptionIsDisable() {
        expectedException.expect(AgentException.class);
        expectedException.expectMessage(AGENT_NAME + ": gui option is disabled");
        jadeSettings.set(JadeSettings.GUI, Boolean.toString(false));
        affectiveModelChartGuiAgent.setup();
    }

}