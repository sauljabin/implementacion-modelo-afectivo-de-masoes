/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import masoes.component.behavioural.emotion.AdmirationEmotion;
import masoes.component.behavioural.emotion.AngerEmotion;
import masoes.component.behavioural.emotion.CompassionEmotion;
import masoes.component.behavioural.emotion.DepressionEmotion;
import masoes.component.behavioural.emotion.HappinessEmotion;
import masoes.component.behavioural.emotion.JoyEmotion;
import masoes.component.behavioural.emotion.RejectionEmotion;
import masoes.component.behavioural.emotion.SadnessEmotion;
import util.RandomGenerator;
import util.ToStringBuilder;

import java.util.Arrays;
import java.util.List;

public class AffectiveModel {

    private static AffectiveModel INSTANCE;
    private List<Emotion> emotions;

    private AffectiveModel() {
        emotions = Arrays.asList(
                new CompassionEmotion(),
                new AdmirationEmotion(),
                new JoyEmotion(),
                new HappinessEmotion(),
                new DepressionEmotion(),
                new SadnessEmotion(),
                new RejectionEmotion(),
                new AngerEmotion());
    }

    public synchronized static AffectiveModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AffectiveModel();
        }
        return INSTANCE;
    }

    public List<Emotion> getEmotions() {
        return emotions;
    }

    public Emotion searchEmotion(EmotionalState emotionalState) {
        return emotions
                .stream()
                .filter(emotion -> emotion.getGeometry().intersects(emotionalState.toPoint()))
                .findFirst()
                .orElse(null);
    }

    public Emotion searchEmotion(String name) {
        return emotions
                .stream()
                .filter(emotion -> emotion.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Emotion getRandomEmotion() {
        return RandomGenerator.getRandomItem(emotions);
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("emotions", emotions)
                .toString();
    }

}
