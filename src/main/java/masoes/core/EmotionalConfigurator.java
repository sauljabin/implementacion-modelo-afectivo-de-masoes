/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;
import masoes.core.emotion.*;
import masoes.util.math.GeometryCreator;

import java.util.ArrayList;
import java.util.List;

public abstract class EmotionalConfigurator {

    private List<Emotion> emotions;
    private EmotionalState emotionalState;

    public EmotionalConfigurator() {
        emotionalState = new EmotionalState(0, 0);
        addEmotions();
    }

    private void addEmotions() {
        emotions = new ArrayList<>();
        emotions.add(new Happiness());
        emotions.add(new Joy());
        emotions.add(new Compassion());
        emotions.add(new Admiration());
        emotions.add(new Depression());
        emotions.add(new Sadness());
        emotions.add(new Dissatisfaction());
        emotions.add(new Rejection());
    }

    public Emotion getEmotion() {
        return getEmotions()
                .stream()
                .filter(emotion -> emotion.getGeometry().intersects(getEmotionalPoint()))
                .findFirst()
                .orElse(new Happiness());
    }

    public List<Emotion> getEmotions() {
        return emotions;
    }

    public Point getEmotionalPoint() {
        return GeometryCreator.createPoint(emotionalState.getActivation(), emotionalState.getSatisfaction());
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void evaluateStimulus(Stimulus stimulus) {
        emotionalState = updateEmotionalState(stimulus);
    }

    protected abstract EmotionalState updateEmotionalState(Stimulus stimulus);

}
