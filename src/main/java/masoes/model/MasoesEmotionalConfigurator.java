/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import com.vividsolutions.jts.geom.Point;
import masoes.core.Emotion;
import masoes.core.EmotionalConfigurator;
import masoes.core.Stimulus;
import masoes.emotion.*;

import java.util.ArrayList;
import java.util.List;

public class MasoesEmotionalConfigurator implements EmotionalConfigurator {

    private List<Emotion> emotions;

    public MasoesEmotionalConfigurator() {
        emotions = new ArrayList<Emotion>();
        emotions.add(new Happiness());
        emotions.add(new Joy());
        emotions.add(new Compassion());
        emotions.add(new Admiration());
        emotions.add(new Depression());
        emotions.add(new Sadness());
        emotions.add(new Dissatisfaction());
        emotions.add(new Rejection());
    }

    @Override
    public List<Emotion> getEmotions() {
        return emotions;
    }

    @Override
    public Point getEmotionalPoint() {
        return null;
    }

    @Override
    public Emotion getEmotionalState() {
        for (Emotion emotion : getEmotions()) {
            if (emotion.getGeometry().intersects(getEmotionalPoint()))
                return emotion;
        }
        return new Happiness();
    }

    @Override
    public void evaluateStimulus(Stimulus stimulus) {

    }
}
