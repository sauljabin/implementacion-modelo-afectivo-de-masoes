/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Environment {

    private ArrayList<AgentCommand> agentCommands;
    private String environmentName;

    public Environment() {
        agentCommands = new ArrayList<>();
    }

    public List<AgentCommand> getAgentCommands() {
        return agentCommands;
    }

    public void add(AgentCommand agentCommand) {
        agentCommands.add(agentCommand);
    }

    public String getEnvironmentName() {
        return Optional.ofNullable(environmentName).orElse(getClass().getSimpleName());
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", getEnvironmentName())
                .toString();
    }

}
