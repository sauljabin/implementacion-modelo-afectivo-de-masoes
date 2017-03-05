/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import environment.AgentParameter;
import environment.Environment;
import environment.wikipedia.creator.CreatorUserAgent;
import gui.RequesterGuiAgent;

import java.util.Arrays;
import java.util.List;

public class WikipediaEnvironment extends Environment {

    @Override
    public List<AgentParameter> getAgentParameters() {
        return Arrays.asList(
                new AgentParameter("creador", CreatorUserAgent.class),
                new AgentParameter("requester", RequesterGuiAgent.class)
        );
    }

    @Override
    public String getName() {
        return "wikipedia";
    }

}
