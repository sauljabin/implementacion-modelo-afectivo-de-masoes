/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import masoes.colective.NotifierAgent;
import settings.SettingsAgent;
import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Environment {

    public static final String SETTINGS_AGENT = "settings";
    public static final String NOTIFIER_AGENT = "notifier";
    private static final String AGENT_DELIMITER = ";";

    public abstract List<AgentParameter> getAgentParameters();

    public abstract String getName();

    public String toJadeParameter() {
        return String.join(AGENT_DELIMITER, toJadeParameterList());
    }

    private List<String> toJadeParameterList() {
        List<AgentParameter> agentParameters = new ArrayList<>();

        if (getAgentParameters() != null) {
            agentParameters.addAll(getAgentParameters());
        }

        agentParameters.add(new AgentParameter(SETTINGS_AGENT, SettingsAgent.class));
        agentParameters.add(new AgentParameter(NOTIFIER_AGENT, NotifierAgent.class));

        return agentParameters.stream().map(
                agentParameter -> agentParameter.toJadeParameter()
        ).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", getName())
                .append("agentParameters", getAgentParameters())
                .toString();
    }

}
