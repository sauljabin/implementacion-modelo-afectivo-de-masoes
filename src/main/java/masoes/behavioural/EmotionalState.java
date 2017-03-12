/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import util.RandomGenerator;
import util.ToStringBuilder;

public class EmotionalState {

    private static final int MAX = 1;
    private static final int MIN = -1;
    private GeometryFactory geometryFactory;
    private double activation;
    private double satisfaction;

    public EmotionalState() {
        RandomGenerator randomGenerator = new RandomGenerator();
        geometryFactory = new GeometryFactory();
        this.activation = randomGenerator.getDouble(MIN, MAX);
        this.satisfaction = randomGenerator.getDouble(MIN, MAX);
    }

    public EmotionalState(double activation, double satisfaction) {
        geometryFactory = new GeometryFactory();
        this.activation = activation;
        this.satisfaction = satisfaction;
    }

    public EmotionalState(Point point) {
        this(point.getX(), point.getY());
    }

    public double getActivation() {
        return activation;
    }

    public double getSatisfaction() {
        return satisfaction;
    }

    public Point toPoint() {
        return geometryFactory.createPoint(new Coordinate(activation, satisfaction));
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .toString();
    }

}
