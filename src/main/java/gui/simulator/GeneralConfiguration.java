/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import gui.simulator.agentconfiguration.AgentConfigurationModel;
import gui.simulator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.simulator.stimulusdefinition.StimulusDefinitionModel;

import java.util.List;

public class GeneralConfiguration {

    private int iterations;
    private List<AgentTypeDefinitionModel> agentTypeDefinitions;
    private List<StimulusDefinitionModel> stimulusDefinitions;
    private List<AgentConfigurationModel> agentConfigurations;

    public GeneralConfiguration() {
    }

    public GeneralConfiguration(int iterations, List<AgentTypeDefinitionModel> agentTypeDefinitions, List<StimulusDefinitionModel> stimulusDefinitions, List<AgentConfigurationModel> agentConfigurations) {
        this.iterations = iterations;
        this.agentTypeDefinitions = agentTypeDefinitions;
        this.stimulusDefinitions = stimulusDefinitions;
        this.agentConfigurations = agentConfigurations;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public List<AgentTypeDefinitionModel> getAgentTypeDefinitions() {
        return agentTypeDefinitions;
    }

    public void setAgentTypeDefinitions(List<AgentTypeDefinitionModel> agentTypeDefinitions) {
        this.agentTypeDefinitions = agentTypeDefinitions;
    }

    public List<StimulusDefinitionModel> getStimulusDefinitions() {
        return stimulusDefinitions;
    }

    public void setStimulusDefinitions(List<StimulusDefinitionModel> stimulusDefinitions) {
        this.stimulusDefinitions = stimulusDefinitions;
    }

    public List<AgentConfigurationModel> getAgentConfigurations() {
        return agentConfigurations;
    }

    public void setAgentConfigurations(List<AgentConfigurationModel> agentConfigurations) {
        this.agentConfigurations = agentConfigurations;
    }

}
