/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.agent;

import environment.wikipedia.configurator.stimulus.StimulusModel;
import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class AgentModel {

    private AgentType agentType;
    private String name;
    private double activation;
    private double satisfaction;
    private List<StimulusModel> stimuli;

    public AgentModel() {
        agentType = AgentType.CONTRIBUTOR;
        name = "";
        activation = 0;
        satisfaction = 0;
        stimuli = new ArrayList<>();
    }

    public AgentType getAgentType() {
        return agentType;
    }

    public void setAgentType(AgentType agentType) {
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

    public List<StimulusModel> getStimuli() {
        return stimuli;
    }

    public void setStimuli(List<StimulusModel> stimuli) {
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
