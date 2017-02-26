/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import environment.AgentParameter;
import environment.Environment;

import java.util.Arrays;
import java.util.List;

public class DummyEnvironment extends Environment {

    private static final String DUMMY = "dummy";

    @Override
    public List<AgentParameter> getAgentParameters() {
        return Arrays.asList(new AgentParameter(DUMMY, DummyEmotionalAgent.class));
    }

    @Override
    public String getName() {
        return DUMMY;
    }

}
