/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.environment.dummy;

import masoes.environment.Environment;
import masoes.environment.EnvironmentAgentInfo;

import java.util.Arrays;
import java.util.List;

public class DummyEnvironment extends Environment {

    private static final String DUMMY_AGENT_NAME = "dummy";

    @Override
    public List<EnvironmentAgentInfo> getEnvironmentAgentInfoList() {
        return Arrays.asList(new EnvironmentAgentInfo(DUMMY_AGENT_NAME, DummyEmotionalAgent.class));
    }

}
