/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import environment.base.AgentCommand;
import environment.base.Environment;

public class DummyEnvironment extends Environment {

    private static final String DUMMY_AGENT_NAME = "dummy";

    public DummyEnvironment() {
        add(new AgentCommand(DUMMY_AGENT_NAME, DummyEmotionalAgent.class));
    }

}
