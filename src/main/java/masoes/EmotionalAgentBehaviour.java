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
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import ontology.masoes.AgentState;
import ontology.masoes.EmotionState;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;

import java.util.Arrays;

public class EmotionalAgentBehaviour extends OntologyResponderBehaviour {

    private EmotionalAgent emotionalAgent;

    public EmotionalAgentBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent, new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), MasoesOntology.getInstance());
        this.emotionalAgent = emotionalAgent;
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        Concept agentAction = action.getAction();
        if (agentAction instanceof GetEmotionalState) {
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
        AgentState agentState = new AgentState();
        agentState.setEmotionName(emotionalAgent.getCurrentEmotion().getName());
        agentState.setBehaviourName(emotionalAgent.getCurrentEmotionalBehaviour().getBehaviourName());
        agentState.setAgent(emotionalAgent.getAID());
        agentState.setEmotionState(new EmotionState(emotionalAgent.getEmotionalState().getActivation(), emotionalAgent.getEmotionalState().getSatisfaction()));
        return agentState;
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetEmotionalState.class, EvaluateStimulus.class)
                .contains(action.getAction().getClass());
    }

}
