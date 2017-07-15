/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import masoes.ontology.state.AgentState;

public class AgentStateWrapper {

    private String stimulusName;
    private AgentState agentState;

    public AgentStateWrapper() {
    }

    public AgentStateWrapper(String stimulusName, AgentState agentState) {
        this.stimulusName = stimulusName;
        this.agentState = agentState;
    }

    public String getStimulusName() {
        return stimulusName;
    }

    public void setStimulusName(String stimulusName) {
        this.stimulusName = stimulusName;
    }

    public AgentState getAgentState() {
        return agentState;
    }

    public void setAgentState(AgentState agentState) {
        this.agentState = agentState;
    }

}
