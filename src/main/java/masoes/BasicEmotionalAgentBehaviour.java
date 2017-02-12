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
import masoes.behavioural.BehaviouralComponent;
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import ontology.masoes.AgentState;
import ontology.masoes.BehaviourState;
import ontology.masoes.EmotionState;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;

import java.util.Arrays;

// TODO: UNIT-TEST

public class BasicEmotionalAgentBehaviour extends OntologyResponderBehaviour {

    private EmotionalAgent emotionalAgent;
    private BehaviouralComponent behaviouralComponent;

    public BasicEmotionalAgentBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent, new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), MasoesOntology.getInstance());
        this.emotionalAgent = emotionalAgent;
        behaviouralComponent = emotionalAgent.getBehaviouralComponent();
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
        behaviouralComponent.evaluateStimulus(agentAction.getStimulus());
        return new Done(action);
    }

    private Predicate responseAgentStatus() {
        EmotionState emotionState = new EmotionState();
        emotionState.setName(behaviouralComponent.getCurrentEmotion().getName());
        emotionState.setClassName(behaviouralComponent.getCurrentEmotion().getClass().getSimpleName());
        emotionState.setActivation(behaviouralComponent.getEmotionalState().getActivation());
        emotionState.setSatisfaction(behaviouralComponent.getEmotionalState().getSatisfaction());

        BehaviourState behaviourState = new BehaviourState();
        behaviourState.setName(behaviouralComponent.getCurrentEmotionalBehaviour().getName());
        behaviourState.setClassName(behaviouralComponent.getCurrentEmotionalBehaviour().getClass().getSimpleName());

        return new AgentState(emotionalAgent.getAID(), emotionState, behaviourState);
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetEmotionalState.class, EvaluateStimulus.class)
                .contains(action.getAction().getClass());
    }

}
