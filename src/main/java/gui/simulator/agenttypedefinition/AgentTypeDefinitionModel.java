/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agenttypedefinition;

import masoes.agent.EmotionalAgent;

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

}
