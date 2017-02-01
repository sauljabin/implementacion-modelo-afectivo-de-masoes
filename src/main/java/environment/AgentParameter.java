/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import jade.core.Agent;
import util.ToStringBuilder;

import java.util.List;
import java.util.Optional;

public class AgentParameter {

    private String agentName;
    private Class<? extends Agent> agentClass;
    private List<String> agentArguments;

    public AgentParameter(String agentName, Class<? extends Agent> agentClass) {
        this.agentName = agentName;
        this.agentClass = agentClass;
    }

    public AgentParameter(String agentName, Class<? extends Agent> agentClass, List<String> agentArguments) {
        this.agentName = agentName;
        this.agentClass = agentClass;
        this.agentArguments = agentArguments;
    }

    public String getAgentName() {
        return agentName;
    }

    public Class<? extends Agent> getAgentClass() {
        return agentClass;
    }

    public List<String> getAgentArguments() {
        return agentArguments;
    }

    public String toJadeParameter() {
        Optional<List<String>> agentArguments = Optional.ofNullable(this.agentArguments);
        if (agentArguments.isPresent() && !agentArguments.get().isEmpty()) {
            return String.format("%s:%s(%s)", agentName, agentClass.getCanonicalName(), String.join(",", this.agentArguments));
        } else {
            return String.format("%s:%s", agentName, agentClass.getCanonicalName());
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("agentName", agentName)
                .append("agentClass", agentClass)
                .append("agentArguments", agentArguments)
                .toString();
    }

}
