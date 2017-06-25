/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import behaviour.CounterBehaviour;
import jade.content.AgentAction;
import jade.core.AID;
import masoes.MasoesSettings;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import ontology.OntologyAssistant;

public class ConfiguratorGuiAgentBehaviour extends CounterBehaviour {

    private final OntologyAssistant assistant;
    private ConfiguratorGuiAgent configuratorAgent;

    public ConfiguratorGuiAgentBehaviour(ConfiguratorGuiAgent configuratorAgent, int maxCount) {
        super(configuratorAgent, maxCount);
        this.configuratorAgent = configuratorAgent;
        assistant = new OntologyAssistant(configuratorAgent, MasoesOntology.getInstance());
    }

    @Override
    public void count(int i) {
        setIteration(i);
        updateStates();
        sleep();
    }

    private void updateStates() {
        configuratorAgent.getAgentTableModel().getAgents().forEach(agent -> {
            AgentAction agentAction = new GetEmotionalState();
            AID receiver = myAgent.getAID(agent.getName());
            AgentState agentState = (AgentState) assistant.sendRequestAction(receiver, agentAction);
            configuratorAgent.getAgentStateTableModel().addAgent(agentState);
        });
    }

    private void setIteration(int i) {
        configuratorAgent.getConfiguratorGui().getIterationLabel().setText(String.valueOf(i));
    }

    private void sleep() {
        myAgent.doWait(1000 / Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.BEHAVIOUR_IPS)));
    }

}
