/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import environment.wikipedia.configurator.agent.AgentType;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;

public class AgentToAdd {

    private int sequence;
    private boolean receiveStimulus;
    private AgentType type;
    private EmotionalState emotionalState;
    private AffectiveModel affectiveModel;

    public AgentToAdd(int sequence, boolean receiveStimulus, AgentType type, EmotionalState emotionalState) {
        this.sequence = sequence;
        this.receiveStimulus = receiveStimulus;
        this.type = type;
        this.emotionalState = emotionalState;
        affectiveModel = AffectiveModel.getInstance();
    }

    public AgentToAdd(int sequence, AgentType type, EmotionalState emotionalState) {
        this(sequence, true, type, emotionalState);
    }

    public AgentType getType() {
        return type;
    }

    public int getSequence() {
        return sequence;
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public String getAgentName() {
        return String.format("%s%d", type.toString(), sequence);
    }

    public String getEmotionalStateString() {
        return String.format("(%.3f, %.3f)", emotionalState.getActivation(), emotionalState.getSatisfaction());
    }

    public String getEmotionName() {
        Emotion emotion = affectiveModel.searchEmotion(emotionalState);
        return String.format("%s - %s", Translation.getInstance().get(emotion.getName().toLowerCase()), Translation.getInstance().get(emotion.getType().toString().toLowerCase()));
    }

    public boolean isReceiveStimulus() {
        return receiveStimulus;
    }

    public void setReceiveStimulus(boolean receiveStimulus) {
        this.receiveStimulus = receiveStimulus;
    }

}
