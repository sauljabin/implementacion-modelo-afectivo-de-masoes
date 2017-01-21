/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.command;

import jade.core.Agent;

import java.util.List;
import java.util.Optional;

public class AgentCommandFormatter {

    private String agentName;
    private Class<? extends Agent> agentClass;
    private List<String> agentArguments;

    public AgentCommandFormatter(String agentName, Class<? extends Agent> agentClass) {
        this.agentName = agentName;
        this.agentClass = agentClass;
    }

    public AgentCommandFormatter(String agentName, Class<? extends Agent> agentClass, List<String> agentArguments) {
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

    public String format() {
        if (Optional.ofNullable(agentArguments).isPresent()) {
            return String.format("%s:%s(%s)", agentName, agentClass.getName(), String.join(",", agentArguments));
        } else {
            return String.format("%s:%s", agentName, agentClass.getName());
        }
    }

}
