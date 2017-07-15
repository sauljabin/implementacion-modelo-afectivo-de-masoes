/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import gui.simulator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.simulator.stimulusdefinition.StimulusDefinitionModel;

import java.util.List;

public class GeneralConfiguration {

    private List<AgentTypeDefinitionModel> agentTypeDefinitionModels;
    private List<StimulusDefinitionModel> stimulusDefinitionModels;

    public GeneralConfiguration() {
    }

    public GeneralConfiguration(List<AgentTypeDefinitionModel> agentTypeDefinitionModels, List<StimulusDefinitionModel> stimulusDefinitionModels) {
        this.agentTypeDefinitionModels = agentTypeDefinitionModels;
        this.stimulusDefinitionModels = stimulusDefinitionModels;
    }

    public List<AgentTypeDefinitionModel> getAgentTypeDefinitionModels() {
        return agentTypeDefinitionModels;
    }

    public void setAgentTypeDefinitionModels(List<AgentTypeDefinitionModel> agentTypeDefinitionModels) {
        this.agentTypeDefinitionModels = agentTypeDefinitionModels;
    }

    public List<StimulusDefinitionModel> getStimulusDefinitionModels() {
        return stimulusDefinitionModels;
    }

    public void setStimulusDefinitionModels(List<StimulusDefinitionModel> stimulusDefinitionModels) {
        this.stimulusDefinitionModels = stimulusDefinitionModels;
    }

}
