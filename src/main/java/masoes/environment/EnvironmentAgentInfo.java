/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.environment;

import jade.core.Agent;

import java.util.List;
import java.util.Optional;

public class EnvironmentAgentInfo {

    private String name;
    private Class<? extends Agent> agentClass;
    private List<String> arguments;

    public EnvironmentAgentInfo(String name, Class<? extends Agent> agentClass) {
        this.name = name;
        this.agentClass = agentClass;
    }

    public EnvironmentAgentInfo(String name, Class<? extends Agent> agentClass, List<String> arguments) {
        this.name = name;
        this.agentClass = agentClass;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Agent> getAgentClass() {
        return agentClass;
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        if (Optional.ofNullable(arguments).isPresent()) {
            return String.format("%s:%s(%s)", name, agentClass.getName(), String.join(",", arguments));
        } else {
            return String.format("%s:%s", name, agentClass.getName());
        }
    }

}
