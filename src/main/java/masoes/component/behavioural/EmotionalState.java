/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

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
        this(RandomGenerator.getDouble(MIN, MAX), RandomGenerator.getDouble(MIN, MAX));
    }

    public EmotionalState(double activation, double satisfaction) {
        geometryFactory = new GeometryFactory();
        setActivation(activation);
        setSatisfaction(satisfaction);
    }

    public EmotionalState(Point point) {
        this(point.getX(), point.getY());
    }

    public double getActivation() {
        return activation;
    }

    public void setActivation(double activation) {
        if (activation > MAX) {
            this.activation = MAX;
        } else if (activation < MIN) {
            this.activation = MIN;
        } else {
            this.activation = activation;
        }
    }

    public double getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(double satisfaction) {
        if (satisfaction > MAX) {
            this.satisfaction = MAX;
        } else if (satisfaction < MIN) {
            this.satisfaction = MIN;
        } else {
            this.satisfaction = satisfaction;
        }
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
