/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.component.behavioural.Emotion;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.stimulus.EvaluateStimulus;
import ontology.ActionResponderBehaviour;

public class EvaluateStimulusBehaviour extends ActionResponderBehaviour {

    private EmotionalAgent emotionalAgent;
    private BehaviouralComponent behaviouralComponent;
    private EmotionalAgentLogger logger;

    public EvaluateStimulusBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent, MasoesOntology.getInstance(), EvaluateStimulus.class);
        this.emotionalAgent = emotionalAgent;
        this.logger = new EmotionalAgentLogger(emotionalAgent);
        this.behaviouralComponent = emotionalAgent.getBehaviouralComponent();
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        EvaluateStimulus evaluateStimulus = (EvaluateStimulus) action.getAction();
        Emotion currentEmotion = behaviouralComponent.getCurrentEmotion();
        behaviouralComponent.evaluateStimulus(evaluateStimulus.getStimulus());
        Emotion newEmotion = behaviouralComponent.getCurrentEmotion();
        logger.updatingEmotion(currentEmotion, newEmotion);
        return new AgentState(emotionalAgent.getAID(), emotionalAgent.getBehaviouralComponent());
    }

}
