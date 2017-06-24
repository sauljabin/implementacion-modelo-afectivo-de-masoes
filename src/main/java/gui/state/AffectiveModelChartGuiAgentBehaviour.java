/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import jade.core.behaviours.CyclicBehaviour;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import ontology.OntologyAssistant;

public class AffectiveModelChartGuiAgentBehaviour extends CyclicBehaviour {

    private static final int FPS = 5;
    private AffectiveModelChartGuiAgent affectiveModelChartGuiAgent;
    private OntologyAssistant masoesOntologyAssistant;

    public AffectiveModelChartGuiAgentBehaviour(AffectiveModelChartGuiAgent affectiveModelChartGuiAgent) {
        this.affectiveModelChartGuiAgent = affectiveModelChartGuiAgent;
        masoesOntologyAssistant = new OntologyAssistant(affectiveModelChartGuiAgent, MasoesOntology.getInstance());
    }

    @Override
    public void action() {
        AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(affectiveModelChartGuiAgent.getEmotionalAgentAID(), new GetEmotionalState());
        affectiveModelChartGuiAgent.getAffectiveModelChartGui().setAgentState(agentState);
        try {
            Thread.sleep(1000 / FPS);
        } catch (InterruptedException e) {
        }
    }

}