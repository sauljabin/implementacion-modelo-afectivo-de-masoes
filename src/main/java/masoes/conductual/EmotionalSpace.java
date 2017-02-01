/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.conductual;

import masoes.conductual.emotion.AdmirationEmotion;
import masoes.conductual.emotion.AngerEmotion;
import masoes.conductual.emotion.CompassionEmotion;
import masoes.conductual.emotion.DepressionEmotion;
import masoes.conductual.emotion.HappinessEmotion;
import masoes.conductual.emotion.JoyEmotion;
import masoes.conductual.emotion.RejectionEmotion;
import masoes.conductual.emotion.SadnessEmotion;

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
