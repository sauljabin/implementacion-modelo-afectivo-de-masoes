/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.Predicate;
import jade.core.AID;
import util.ToStringBuilder;

public class AgentState implements Predicate {

    private AID agent;
    private String behaviourName;
    private String emotionName;
    private EmotionState emotionState;

    public AgentState() {
    }

    public AgentState(AID agent, String behaviourName, String emotionName, EmotionState emotionState) {
        this.agent = agent;
        this.behaviourName = behaviourName;
        this.emotionName = emotionName;
        this.emotionState = emotionState;
    }

    public EmotionState getEmotionState() {
        return emotionState;
    }

    public void setEmotionState(EmotionState emotionState) {
        this.emotionState = emotionState;
    }

    public AID getAgent() {
        return agent;
    }

    public void setAgent(AID agent) {
        this.agent = agent;
    }

    public String getBehaviourName() {
        return behaviourName;
    }

    public void setBehaviourName(String behaviourName) {
        this.behaviourName = behaviourName;
    }

    public String getEmotionName() {
        return emotionName;
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("agent", agent)
                .append("behaviourName", behaviourName)
                .append("emotionName", emotionName)
                .append("emotionState", emotionState)
                .toString();
    }

}
