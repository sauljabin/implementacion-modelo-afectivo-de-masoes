/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;
import masoes.core.emotion.*;
import masoes.util.math.RandomGenerator;

import java.util.Arrays;
import java.util.List;

public abstract class EmotionalConfigurator {

    private List<Emotion> emotions;
    private EmotionalState emotionalState;
    private Point emotionalPoint;
    private Emotion emotion;

    public EmotionalConfigurator() {
        addEmotions();
        initEmotionalState();
    }

    private void addEmotions() {
        emotions = Arrays.asList(
                new Happiness(),
                new Joy(),
                new Compassion(),
                new Admiration(),
                new Depression(),
                new Sadness(),
                new Dissatisfaction(),
                new Rejection());
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
        return emotions;
    }

    public Point getEmotionalPoint() {
        return emotionalPoint;
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void updateEmotionalState(Stimulus stimulus) {
        emotionalState = evaluateStimulus(stimulus);
        emotionalPoint = emotionalState.toPoint();
        emotion = searchEmotion();
    }

    protected abstract EmotionalState evaluateStimulus(Stimulus stimulus);

}
