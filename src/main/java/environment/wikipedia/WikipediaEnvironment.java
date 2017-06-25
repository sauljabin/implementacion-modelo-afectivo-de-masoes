/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import environment.AgentParameter;
import environment.Environment;
import environment.wikipedia.configurator.ConfiguratorGuiAgent;

import java.util.Arrays;
import java.util.List;

public class WikipediaEnvironment extends Environment {

    private static final String WIKIPEDIA = "wikipedia";

    @Override
    public List<AgentParameter> getAgentParameters() {
        return Arrays.asList(
                new AgentParameter("configurator", ConfiguratorGuiAgent.class)
        );
    }

    @Override
    public String getName() {
        return WIKIPEDIA;
    }

}
