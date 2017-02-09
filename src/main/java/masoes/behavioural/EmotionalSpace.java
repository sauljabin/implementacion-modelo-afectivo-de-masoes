/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import masoes.behavioural.emotion.AdmirationEmotion;
import masoes.behavioural.emotion.AngerEmotion;
import masoes.behavioural.emotion.CompassionEmotion;
import masoes.behavioural.emotion.DepressionEmotion;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.JoyEmotion;
import masoes.behavioural.emotion.RejectionEmotion;
import masoes.behavioural.emotion.SadnessEmotion;

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
