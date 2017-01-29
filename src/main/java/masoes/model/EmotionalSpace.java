/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.model.emotion.AdmirationEmotion;
import masoes.model.emotion.AngerEmotion;
import masoes.model.emotion.CompassionEmotion;
import masoes.model.emotion.DepressionEmotion;
import masoes.model.emotion.HappinessEmotion;
import masoes.model.emotion.JoyEmotion;
import masoes.model.emotion.RejectionEmotion;
import masoes.model.emotion.SadnessEmotion;

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
