/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.ontology;

import jade.content.Predicate;
import jade.core.AID;

public class AgentStatus implements Predicate {

    private AID agent;
    private String behaviourName;
    private String emotionName;
    private EmotionStatus emotionStatus;

    public AgentStatus() {
    }

    public AgentStatus(AID agent, String behaviourName, String emotionName, EmotionStatus emotionStatus) {
        this.agent = agent;
        this.behaviourName = behaviourName;
        this.emotionName = emotionName;
        this.emotionStatus = emotionStatus;
    }

    public EmotionStatus getEmotionStatus() {
        return emotionStatus;
    }

    public void setEmotionStatus(EmotionStatus emotionStatus) {
        this.emotionStatus = emotionStatus;
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
        final StringBuffer sb = new StringBuffer("AgentStatus{");
        sb.append("agent=").append(agent);
        sb.append(", behaviourName='").append(behaviourName).append('\'');
        sb.append(", emotionName='").append(emotionName).append('\'');
        sb.append(", emotionStatus=").append(emotionStatus);
        sb.append('}');
        return sb.toString();
    }
}
