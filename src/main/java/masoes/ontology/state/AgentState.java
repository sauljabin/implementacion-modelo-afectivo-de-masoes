/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.state;

import jade.content.Predicate;
import jade.core.AID;
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
