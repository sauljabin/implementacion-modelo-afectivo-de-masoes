/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.content.Concept;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import ontology.masoes.AgentStatus;
import ontology.masoes.EmotionStatus;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetAgentStatus;
import ontology.masoes.MasoesOntology;
import protocol.OntologyResponderBehaviour;

import java.util.Arrays;

public class EmotionalAgentBehaviour extends OntologyResponderBehaviour {

    private EmotionalAgent emotionalAgent;

    public EmotionalAgentBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent, new MessageTemplate(new MasoesMatchExpression()), new MasoesOntology());
        this.emotionalAgent = emotionalAgent;
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        Concept agentAction = action.getAction();

        if (agentAction instanceof GetAgentStatus) {
            return responseAgentStatus();
        } else {
            return responseEvaluateStimulus(action);
        }
    }

    private Predicate responseEvaluateStimulus(Action action) {
        EvaluateStimulus agentAction = (EvaluateStimulus) action.getAction();
        emotionalAgent.evaluateStimulus(agentAction.getStimulus());
        return new Done(action);
    }

    private Predicate responseAgentStatus() {
        AgentStatus agentStatus = new AgentStatus();
        agentStatus.setEmotionName(emotionalAgent.getCurrentEmotion().getEmotionName());
        agentStatus.setBehaviourName(emotionalAgent.getCurrentEmotionalBehaviour().getBehaviourName());
        agentStatus.setAgent(emotionalAgent.getAID());
        agentStatus.setEmotionStatus(new EmotionStatus(emotionalAgent.getEmotionalState().getActivation(), emotionalAgent.getEmotionalState().getSatisfaction()));
        return agentStatus;
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetAgentStatus.class, EvaluateStimulus.class)
                .contains(action.getAction().getClass());
    }

}
