/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;
import util.ToStringBuilder;

public abstract class Emotion {

    private RandomPointsBuilder randomPointsBuilder;
    private Geometry polygon;
    private GeometryFactory geometryFactory;

    public Emotion() {
        geometryFactory = new GeometryFactory();
        polygon = geometryFactory.createPolygon(getCoordinates());
        randomPointsBuilder = new RandomPointsBuilder();
        randomPointsBuilder.setExtent(polygon);
        randomPointsBuilder.setNumPoints(1);
    }

    public Geometry getGeometry() {
        return polygon;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", getName())
                .append("type", getType())
                .append("level", getLevel())
                .toString();
    }

    public Point getRandomPoint() {
        return randomPointsBuilder.getGeometry().getInteriorPoint();
    }

    public EmotionalState getRandomEmotionalState() {
        return new EmotionalState(getRandomPoint());
    }

    public abstract String getName();

    public abstract Coordinate[] getCoordinates();

    public abstract EmotionType getType();

    public abstract EmotionLevel getLevel();

}
