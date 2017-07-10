/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agentconfiguration;

import gui.configurator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.configurator.stimulusdefinition.StimulusDefinitionModel;
import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class AgentConfigurationModel {

    private AgentTypeDefinitionModel agentType;
    private String name;
    private double activation;
    private double satisfaction;
    private List<StimulusDefinitionModel> stimuli;

    public AgentConfigurationModel() {
        this(null, "", 0, 0, new ArrayList<>());
    }

    public AgentConfigurationModel(AgentTypeDefinitionModel agentType, String name, double activation, double satisfaction, List<StimulusDefinitionModel> stimuli) {
        this.agentType = agentType;
        this.name = name;
        this.activation = activation;
        this.satisfaction = satisfaction;
        this.stimuli = stimuli;
    }

    public AgentTypeDefinitionModel getAgentType() {
        return agentType;
    }

    public void setAgentType(AgentTypeDefinitionModel agentType) {
        this.agentType = agentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getActivation() {
        return activation;
    }

    public void setActivation(double activation) {
        this.activation = activation;
    }

    public double getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(double satisfaction) {
        this.satisfaction = satisfaction;
    }

    public List<StimulusDefinitionModel> getStimuli() {
        return stimuli;
    }

    public void setStimuli(List<StimulusDefinitionModel> stimuli) {
        this.stimuli = stimuli;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("agentType", agentType)
                .append("name", name)
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .append("stimuli", stimuli)
                .toString();
    }

}
