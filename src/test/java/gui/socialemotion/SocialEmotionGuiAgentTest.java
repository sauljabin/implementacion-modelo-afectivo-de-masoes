/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

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

public class SocialEmotionGuiAgentTest {

    private static final String AGENT_NAME = "agentName";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private SocialEmotionGui socialEmotionGuiMock;
    private SocialEmotionGuiAgent socialEmotionGuiAgent;
    private JadeSettings jadeSettings;

    @Before
    public void setUp() throws Exception {
        socialEmotionGuiMock = mock(SocialEmotionGui.class);

        socialEmotionGuiAgent = new SocialEmotionGuiAgent();
        setFieldValue(socialEmotionGuiAgent, "socialEmotionGui", socialEmotionGuiMock);
        setFieldValue(socialEmotionGuiAgent, "myName", AGENT_NAME);
        jadeSettings = JadeSettings.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(jadeSettings, "INSTANCE", null);
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        socialEmotionGuiAgent.takeDown();
        verify(socialEmotionGuiMock).closeGui();
    }

    @Test
    public void shouldThrowExceptionWhenJadeGuiOptionIsDisable() {
        expectedException.expect(AgentException.class);
        expectedException.expectMessage(AGENT_NAME + ": gui option is disabled");
        jadeSettings.set(JadeSettings.GUI, Boolean.toString(false));
        socialEmotionGuiAgent.setup();
    }

}