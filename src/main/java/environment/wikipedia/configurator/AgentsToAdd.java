/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

public class AgentsToAdd {

    private AgentToAddType type;
    private EmotionToAdd emotion;
    private int sequence;

    public AgentsToAdd() {
    }

    public AgentsToAdd(AgentToAddType type, EmotionToAdd emotion, int sequence) {
        this.type = type;
        this.emotion = emotion;
        this.sequence = sequence;
    }

    public AgentToAddType getType() {
        return type;
    }

    public void setType(AgentToAddType type) {
        this.type = type;
    }

    public EmotionToAdd getEmotion() {
        return emotion;
    }

    public void setEmotion(EmotionToAdd emotion) {
        this.emotion = emotion;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return String.format("%s%d", type.toString(), sequence);
    }

}
