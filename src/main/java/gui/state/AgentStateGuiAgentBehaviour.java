/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import jade.core.behaviours.CyclicBehaviour;
import masoes.MasoesSettings;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import ontology.OntologyAssistant;
import util.StopWatch;

public class AgentStateGuiAgentBehaviour extends CyclicBehaviour {

    private static final long WAIT = 1000 / Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.BEHAVIOUR_IPS));
    private AgentStateGuiAgent agentStateGuiAgent;
    private OntologyAssistant masoesOntologyAssistant;
    private StopWatch stopWatch;

    public AgentStateGuiAgentBehaviour(AgentStateGuiAgent agentStateGuiAgent) {
        this.agentStateGuiAgent = agentStateGuiAgent;
        masoesOntologyAssistant = new OntologyAssistant(agentStateGuiAgent, MasoesOntology.getInstance());
        stopWatch = new StopWatch();
    }

    @Override
    public void action() {
        AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(agentStateGuiAgent.getEmotionalAgentAID(), new GetEmotionalState());
        agentStateGuiAgent.getAgentStateGui().setAgentState(agentState);
        sleep();
    }

    private void sleep() {
        stopWatch.start();
        while (stopWatch.getTime() < WAIT) {
            continue;
        }
        stopWatch.stop();
    }

}
