/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Environment {

    private static final String AGENT_DELIMITER = ";";
    private ArrayList<AgentParameter> agentParameters;
    private String name;

    public Environment(String name) {
        this.name = name;
        agentParameters = new ArrayList<>();
    }

    public Environment() {
        this(null);
    }

    public List<AgentParameter> getAgentParameters() {
        return agentParameters;
    }

    public void add(AgentParameter agentParameter) {
        agentParameters.add(agentParameter);
    }

    public String getName() {
        return Optional.ofNullable(name).orElse(getClass().getSimpleName());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJadeParameter() {
        return String.join(AGENT_DELIMITER, toJadeParameterList());
    }

    public List<String> toJadeParameterList() {
        return agentParameters.stream().map(
                agentParameter -> agentParameter.toJadeParameter()
        ).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", getName())
                .append("agentParameters", agentParameters)
                .toString();
    }

    public void remove(AgentParameter agentParameter) {
        agentParameters.remove(agentParameter);
    }

}
