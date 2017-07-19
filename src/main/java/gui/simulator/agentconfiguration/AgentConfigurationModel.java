/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agentconfiguration;

import gui.simulator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.simulator.stimulusconfiguration.StimulusConfigurationModel;
import masoes.component.behavioural.EmotionalState;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class AgentConfigurationModel {

    private AgentTypeDefinitionModel agentType;
    private String name;
    private double activation;
    private double satisfaction;
    private List<StimulusConfigurationModel> stimulusConfigurations;

    public AgentConfigurationModel() {
        this(null, "", 0, 0, new ArrayList<>());
    }

    public AgentConfigurationModel(AgentTypeDefinitionModel agentType, String name, EmotionalState emotionalState, List<StimulusConfigurationModel> stimulusConfigurations) {
        this.agentType = agentType;
        this.name = name;
        this.stimulusConfigurations = stimulusConfigurations;
        this.activation = emotionalState.getActivation();
        this.satisfaction = emotionalState.getSatisfaction();
    }

    public AgentConfigurationModel(AgentTypeDefinitionModel agentType, String name, double activation, double satisfaction, List<StimulusConfigurationModel> stimulusConfigurations) {
        this.agentType = agentType;
        this.name = name;
        this.activation = activation;
        this.satisfaction = satisfaction;
        this.stimulusConfigurations = stimulusConfigurations;
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

    public List<StimulusConfigurationModel> getStimulusConfigurations() {
        return stimulusConfigurations;
    }

    public void setStimulusConfigurations(List<StimulusConfigurationModel> stimulusConfigurations) {
        this.stimulusConfigurations = stimulusConfigurations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("agentType", agentType)
                .append("name", name)
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .append("stimulusConfigurations", stimulusConfigurations)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AgentConfigurationModel that = (AgentConfigurationModel) o;

        return new EqualsBuilder()
                .append(activation, that.activation)
                .append(satisfaction, that.satisfaction)
                .append(agentType, that.agentType)
                .append(name, that.name)
                .append(stimulusConfigurations, that.stimulusConfigurations)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(agentType)
                .append(name)
                .append(activation)
                .append(satisfaction)
                .append(stimulusConfigurations)
                .toHashCode();
    }

}
