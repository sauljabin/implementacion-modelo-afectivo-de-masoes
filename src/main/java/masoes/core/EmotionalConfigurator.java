/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import masoes.util.math.RandomGenerator;

public abstract class EmotionalConfigurator {

    private EmotionalState emotionalState;
    private EmotionalSpace emotionalSpace;

    public EmotionalConfigurator() {
        RandomGenerator random = new RandomGenerator();
        emotionalState = new EmotionalState(random.getDouble(-1, 1), random.getDouble(-1, 1));
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

    protected abstract EmotionalState calculateEmotionalState(Stimulus stimulus);

}
