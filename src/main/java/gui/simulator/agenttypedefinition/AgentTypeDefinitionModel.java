/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agenttypedefinition;

import masoes.agent.EmotionalAgent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AgentTypeDefinitionModel {

    private Class<? extends EmotionalAgent> agentTypeClass;
    private String agentTypeName;

    public AgentTypeDefinitionModel() {
    }

    public AgentTypeDefinitionModel(Class<? extends EmotionalAgent> agentTypeClass, String agentTypeName) {
        this.agentTypeClass = agentTypeClass;
        this.agentTypeName = agentTypeName;
    }

    public Class<? extends EmotionalAgent> getAgentTypeClass() {
        return agentTypeClass;
    }

    public void setAgentTypeClass(Class<? extends EmotionalAgent> agentTypeClass) {
        this.agentTypeClass = agentTypeClass;
    }

    public String getAgentTypeName() {
        return agentTypeName;
    }

    public void setAgentTypeName(String agentTypeName) {
        this.agentTypeName = agentTypeName;
    }

    @Override
    public String toString() {
        return agentTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AgentTypeDefinitionModel that = (AgentTypeDefinitionModel) o;

        return new EqualsBuilder()
                .append(agentTypeClass, that.agentTypeClass)
                .append(agentTypeName, that.agentTypeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(agentTypeClass)
                .append(agentTypeName)
                .toHashCode();
    }

}
