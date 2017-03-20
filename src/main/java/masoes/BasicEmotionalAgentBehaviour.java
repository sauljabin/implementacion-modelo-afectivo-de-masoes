/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
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
import ontology.masoes.state.AgentState;
import ontology.masoes.state.BehaviourState;
import ontology.masoes.state.EmotionState;
import ontology.masoes.stimulus.EvaluateStimulus;
import ontology.masoes.state.GetEmotionalState;
import ontology.masoes.MasoesOntology;

import java.util.Arrays;

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
        emotionalAgent.getLogger().receivingStimulus(agentAction.getStimulus());
        behaviouralComponent.evaluateStimulus(agentAction.getStimulus());
        emotionalAgent.getLogger().sendingDone();
        return new Done(action);
    }

    private Predicate responseAgentStatus() {
        EmotionState emotionState = new EmotionState();
        emotionState.setName(behaviouralComponent.getCurrentEmotion().getName());
        emotionState.setClassName(behaviouralComponent.getCurrentEmotion().getClass().getSimpleName());
        emotionState.setActivation(behaviouralComponent.getCurrentEmotionalState().getActivation());
        emotionState.setSatisfaction(behaviouralComponent.getCurrentEmotionalState().getSatisfaction());
        emotionState.setType(behaviouralComponent.getCurrentEmotion().getType().toString());

        BehaviourState behaviourState = new BehaviourState();
        behaviourState.setName(behaviouralComponent.getCurrentEmotionalBehaviour().getName());
        behaviourState.setClassName(behaviouralComponent.getCurrentEmotionalBehaviour().getClass().getSimpleName());
        behaviourState.setType(behaviouralComponent.getCurrentEmotionalBehaviour().getType().toString());

        return new AgentState(emotionalAgent.getAID(), emotionState, behaviourState);
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetEmotionalState.class, EvaluateStimulus.class)
                .contains(action.getAction().getClass());
    }

}
