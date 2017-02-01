/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import ontology.masoes.AgentStatus;
import ontology.masoes.EmotionStatus;
import ontology.masoes.GetAgentStatus;
import ontology.masoes.MasoesOntology;
import protocol.OntologyResponderBehaviour;

import java.util.Arrays;

public class ResponseAgentStatusBehaviour extends OntologyResponderBehaviour {

    private EmotionalAgent emotionalAgent;

    public ResponseAgentStatusBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent, new MasoesRequestMessageTemplate(), new MasoesOntology());
        this.emotionalAgent = emotionalAgent;
    }

    @Override
    public Predicate performAction(Action action) {
        AgentStatus agentStatus = new AgentStatus();
        agentStatus.setEmotionName(emotionalAgent.getCurrentEmotion().getEmotionName());
        agentStatus.setBehaviourName(emotionalAgent.getCurrentEmotionalBehaviour().getBehaviourName());
        agentStatus.setAgent(emotionalAgent.getAID());
        agentStatus.setEmotionStatus(new EmotionStatus(emotionalAgent.getEmotionalState().getActivation(), emotionalAgent.getEmotionalState().getSatisfaction()));
        return agentStatus;
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetAgentStatus.class)
                .contains(action.getAction().getClass());
    }

}
