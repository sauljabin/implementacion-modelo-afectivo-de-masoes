/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.simulation;

import environment.AgentParameter;
import environment.Environment;
import gui.simulator.SimulatorGuiAgent;

import java.util.Arrays;
import java.util.List;

public class SimulationEnvironment extends Environment {

    private static final String SIMULATION = "simulation";

    @Override
    public List<AgentParameter> getAgentParameters() {
        return Arrays.asList(
                new AgentParameter("simulator", SimulatorGuiAgent.class)
        );
    }

    @Override
    public String getName() {
        return SIMULATION;
    }

}
