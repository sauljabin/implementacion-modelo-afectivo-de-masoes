/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.state;

import jade.content.Predicate;
import jade.core.AID;
import masoes.component.behavioural.BehaviouralComponent;
import util.ToStringBuilder;

public class AgentState implements Predicate {

    private AID agent;
    private EmotionState emotionState;
    private BehaviourState behaviourState;

    public AgentState() {
    }

    public AgentState(AID agent, EmotionState emotionState, BehaviourState behaviourState) {
        this.agent = agent;
        this.emotionState = emotionState;
        this.behaviourState = behaviourState;
    }

    public AgentState(AID agent, BehaviouralComponent behaviouralComponent) {
        this.agent = agent;
        this.emotionState = new EmotionState();
        this.emotionState.setName(behaviouralComponent.getCurrentEmotion().getName());
        this.emotionState.setClassName(behaviouralComponent.getCurrentEmotion().getClass().getSimpleName());
        this.emotionState.setActivation(behaviouralComponent.getCurrentEmotionalState().getActivation());
        this.emotionState.setSatisfaction(behaviouralComponent.getCurrentEmotionalState().getSatisfaction());
        this.emotionState.setType(behaviouralComponent.getCurrentEmotion().getType().toString());

        this.behaviourState = new BehaviourState();
        this.behaviourState.setType(behaviouralComponent.getCurrentBehaviourType().toString());
    }

    public AID getAgent() {
        return agent;
    }

    public void setAgent(AID agent) {
        this.agent = agent;
    }

    public EmotionState getEmotionState() {
        return emotionState;
    }

    public void setEmotionState(EmotionState emotionState) {
        this.emotionState = emotionState;
    }

    public BehaviourState getBehaviourState() {
        return behaviourState;
    }

    public void setBehaviourState(BehaviourState behaviourState) {
        this.behaviourState = behaviourState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("agent", agent)
                .append("emotionState", emotionState)
                .append("behaviourState", behaviourState)
                .toString();
    }

}
