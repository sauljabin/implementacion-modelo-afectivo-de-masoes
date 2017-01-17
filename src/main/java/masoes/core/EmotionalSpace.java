/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import masoes.core.emotion.AdmirationEmotion;
import masoes.core.emotion.AngerEmotion;
import masoes.core.emotion.CompassionEmotion;
import masoes.core.emotion.DepressionEmotion;
import masoes.core.emotion.HappinessEmotion;
import masoes.core.emotion.JoyEmotion;
import masoes.core.emotion.RejectionEmotion;
import masoes.core.emotion.SadnessEmotion;

import java.util.Arrays;
import java.util.List;

public class EmotionalSpace {

    private List<Emotion> emotions;

    public EmotionalSpace() {
        emotions = Arrays.asList(
                new HappinessEmotion(),
                new JoyEmotion(),
                new CompassionEmotion(),
                new AdmirationEmotion(),
                new DepressionEmotion(),
                new SadnessEmotion(),
                new AngerEmotion(),
                new RejectionEmotion());
    }

    public Emotion searchEmotion(EmotionalState emotionalState) {
        return emotions
                .stream()
                .filter(emotion -> emotion.getGeometry().intersects(emotionalState.toPoint()))
                .findFirst()
                .orElse(new HappinessEmotion());
    }

}
