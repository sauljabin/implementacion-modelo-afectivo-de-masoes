/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;
import masoes.core.emotion.Admiration;
import masoes.core.emotion.Compassion;
import masoes.core.emotion.Depression;
import masoes.core.emotion.Dissatisfaction;
import masoes.core.emotion.Happiness;
import masoes.core.emotion.Joy;
import masoes.core.emotion.Rejection;
import masoes.core.emotion.Sadness;
import masoes.util.math.RandomGenerator;

import java.util.Arrays;
import java.util.List;

public abstract class EmotionalConfigurator {

    private EmotionalState emotionalState;
    private Point emotionalPoint;
    private Emotion emotion;

    public EmotionalConfigurator() {
        initEmotionalState();
    }

    private void initEmotionalState() {
        RandomGenerator random = new RandomGenerator();
        emotionalState = new EmotionalState(random.getDouble(-1, 1), random.getDouble(-1, 1));
        emotionalPoint = emotionalState.toPoint();
        emotion = searchEmotion();
    }

    private Emotion searchEmotion() {
        return getEmotions()
                .stream()
                .filter(emotion -> emotion.getGeometry().intersects(getEmotionalPoint()))
                .findFirst()
                .orElse(new Happiness());
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public List<Emotion> getEmotions() {
        return Arrays.asList(
                new Happiness(),
                new Joy(),
                new Compassion(),
                new Admiration(),
                new Depression(),
                new Sadness(),
                new Dissatisfaction(),
                new Rejection());
    }

    public Point getEmotionalPoint() {
        return emotionalPoint;
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void updateEmotionalState(Stimulus stimulus) {
        emotionalState = calculateEmotionalState(stimulus);
        emotionalPoint = emotionalState.toPoint();
        emotion = searchEmotion();
    }

    protected abstract EmotionalState calculateEmotionalState(Stimulus stimulus);

}
