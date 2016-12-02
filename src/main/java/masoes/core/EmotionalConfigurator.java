/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

public class EmotionalConfigurator {

    private EmotionalState emotionalState;
    private EmotionalSpace emotionalSpace;

    public EmotionalConfigurator() {
        emotionalState = new EmotionalState();
        emotionalSpace = new EmotionalSpace();
    }

    public Emotion getEmotion() {
        return emotionalSpace.searchEmotion(emotionalState);
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void updateEmotionalState(Stimulus stimulus) {
        emotionalState = calculateEmotionalState(stimulus);
    }

    protected EmotionalState calculateEmotionalState(Stimulus stimulus) {
        // TODO: Implementar este método
        return new EmotionalState();
    }

}
