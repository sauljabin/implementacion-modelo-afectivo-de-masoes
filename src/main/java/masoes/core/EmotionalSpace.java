/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import masoes.core.emotion.Admiration;
import masoes.core.emotion.Anger;
import masoes.core.emotion.Compassion;
import masoes.core.emotion.Depression;
import masoes.core.emotion.Happiness;
import masoes.core.emotion.Joy;
import masoes.core.emotion.Rejection;
import masoes.core.emotion.Sadness;

import java.util.Arrays;
import java.util.List;

public class EmotionalSpace {

    private List<Emotion> emotions;

    public EmotionalSpace() {
        emotions = Arrays.asList(
                new Happiness(),
                new Joy(),
                new Compassion(),
                new Admiration(),
                new Depression(),
                new Sadness(),
                new Anger(),
                new Rejection());
    }

    public Emotion searchEmotion(EmotionalState emotionalState) {
        return emotions
                .stream()
                .filter(emotion -> emotion.getGeometry().intersects(emotionalState.toPoint()))
                .findFirst()
                .orElse(new Happiness());
    }

}
