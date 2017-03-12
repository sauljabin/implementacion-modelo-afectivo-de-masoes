/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import environment.AgentParameter;
import environment.Environment;
import environment.wikipedia.reviewer.ReviewerUserAgent;
import environment.wikipedia.submitter.SubmitterUserAgent;
import gui.RequesterGuiAgent;

import java.util.Arrays;
import java.util.List;

public class WikipediaEnvironment extends Environment {

    @Override
    public List<AgentParameter> getAgentParameters() {
        return Arrays.asList(
                new AgentParameter("submitter", SubmitterUserAgent.class),
                new AgentParameter("reviewer", ReviewerUserAgent.class),
                new AgentParameter("requester", RequesterGuiAgent.class)
        );
    }

    @Override
    public String getName() {
        return "wikipedia";
    }

}
