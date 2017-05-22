/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalSpace;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;

public class AgentToAdd {

    private int sequence;
    private boolean receiveStimulus;
    private AgentTypeToAdd type;
    private EmotionalState emotionalState;
    private EmotionalSpace emotionalSpace;

    public AgentToAdd(int sequence, boolean receiveStimulus, AgentTypeToAdd type, EmotionalState emotionalState) {
        this.sequence = sequence;
        this.receiveStimulus = receiveStimulus;
        this.type = type;
        this.emotionalState = emotionalState;
        emotionalSpace = new EmotionalSpace();
    }

    public AgentToAdd(int sequence, AgentTypeToAdd type, EmotionalState emotionalState) {
        this(sequence, true, type, emotionalState);
    }

    public AgentTypeToAdd getType() {
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
        Emotion emotion = emotionalSpace.searchEmotion(emotionalState);
        return String.format("%s - %s", Translation.getInstance().get(emotion.getName().toLowerCase()), Translation.getInstance().get(emotion.getType().toString().toLowerCase()));
    }

    public boolean isReceiveStimulus() {
        return receiveStimulus;
    }

    public void setReceiveStimulus(boolean receiveStimulus) {
        this.receiveStimulus = receiveStimulus;
    }
    
}
