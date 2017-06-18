/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import environment.AgentParameter;
import environment.Environment;
import gui.requester.RequesterGuiAgent;
import gui.state.AffectiveModelChartGuiAgent;

import java.util.Arrays;
import java.util.List;

public class DummyEnvironment extends Environment {

    private static final String DUMMY = "dummy";
    private static final String DUMMY_GUI = "dummyGUI";
    private static final String REQUESTER = "requester";

    @Override
    public List<AgentParameter> getAgentParameters() {
        return Arrays.asList(
                new AgentParameter(DUMMY, DummyEmotionalAgent.class),
                new AgentParameter(DUMMY_GUI, AffectiveModelChartGuiAgent.class, Arrays.asList(DUMMY)),
                new AgentParameter(REQUESTER, RequesterGuiAgent.class)
        );
    }

    @Override
    public String getName() {
        return DUMMY;
    }

}
