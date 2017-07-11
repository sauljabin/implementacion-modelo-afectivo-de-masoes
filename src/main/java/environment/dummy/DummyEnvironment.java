/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import environment.AgentParameter;
import environment.Environment;
import gui.agentstate.AgentStateGuiAgent;
import gui.requester.RequesterGuiAgent;
import gui.socialemotion.SocialEmotionGuiAgent;

import java.util.Arrays;
import java.util.List;

public class DummyEnvironment extends Environment {

    private static final String DUMMY = "dummy";
    private static final String DUMMY_EMOTIONAL_AGENT = "dummyEmotionalAgent";
    private static final String DUMMY_EMOTIONAL_AGENT_GUI = DUMMY_EMOTIONAL_AGENT + "GUI";
    private static final String SOCIAL_EMOTION_AGENT_GUI = SOCIAL_EMOTION_AGENT + "GUI";
    private static final String REQUESTER = "requester";

    @Override
    public List<AgentParameter> getAgentParameters() {
        return Arrays.asList(
                new AgentParameter(DUMMY, DummyAgent.class),
                new AgentParameter(DUMMY_EMOTIONAL_AGENT, DummyEmotionalAgent.class),
                new AgentParameter(DUMMY_EMOTIONAL_AGENT_GUI, AgentStateGuiAgent.class, Arrays.asList(DUMMY_EMOTIONAL_AGENT)),
                new AgentParameter(REQUESTER, RequesterGuiAgent.class),
                new AgentParameter(SOCIAL_EMOTION_AGENT_GUI, SocialEmotionGuiAgent.class)
        );
    }

    @Override
    public String getName() {
        return DUMMY;
    }

}
