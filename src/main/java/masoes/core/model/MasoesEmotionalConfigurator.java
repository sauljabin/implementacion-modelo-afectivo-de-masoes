/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import masoes.core.Emotion;
import masoes.core.EmotionalConfigurator;
import masoes.core.Stimulus;
import masoes.core.emotion.*;
import masoes.core.stimulus.InitialStimulus;
import masoes.util.math.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class MasoesEmotionalConfigurator implements EmotionalConfigurator {

    private final GeometryFactory geometryFactory;
    private final RandomGenerator randomGenerator;
    private List<Emotion> emotions;
    private double activation;
    private double satisfaction;

    public MasoesEmotionalConfigurator() {
        geometryFactory = new GeometryFactory();
        randomGenerator = new RandomGenerator();
        addEmotions();
        evaluateStimulus(new InitialStimulus());
    }

    private void addEmotions() {
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
        return geometryFactory.createPoint(new Coordinate(getActivation(), getSatisfaction()));
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
        if (stimulus instanceof InitialStimulus) {
            activation = randomGenerator.random(-1, 1);
            satisfaction = randomGenerator.random(-1, 1);
        }
    }

    @Override
    public double getActivation() {
        return activation;
    }

    @Override
    public double getSatisfaction() {
        return satisfaction;
    }
}
