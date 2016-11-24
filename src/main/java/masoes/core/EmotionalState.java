/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;
import masoes.util.math.GeometryCreator;
import masoes.util.math.RandomGenerator;

public class EmotionalState {

    private GeometryCreator geometryCreator;
    private double activation;
    private double satisfaction;

    public EmotionalState() {
        this(getRandomDouble(), getRandomDouble());
    }

    public EmotionalState(double activation, double satisfaction) {
        this.activation = activation;
        this.satisfaction = satisfaction;
        geometryCreator = new GeometryCreator();
    }

    private static double getRandomDouble() {
        return new RandomGenerator().getDouble(-1, 1);
    }

    public double getActivation() {
        return activation;
    }

    public double getSatisfaction() {
        return satisfaction;
    }

    public Point toPoint() {
        return geometryCreator.createPoint(activation, satisfaction);
    }

    @Override
    public String toString() {
        return String.format("{activation=%s, satisfaction=%s}", activation, satisfaction);
    }

}
