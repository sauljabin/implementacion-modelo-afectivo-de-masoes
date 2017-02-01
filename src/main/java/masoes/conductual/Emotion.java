/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.conductual;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;
import util.ToStringBuilder;

import java.util.Optional;

public abstract class Emotion {

    private RandomPointsBuilder randomPointsBuilder;
    private Geometry polygon;
    private GeometryFactory geometryFactory;
    private String emotionName;

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

    public String getEmotionName() {
        return Optional.ofNullable(emotionName).orElse(getClass().getSimpleName());
    }

    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", getEmotionName())
                .append("type", getEmotionType())
                .append("level", getEmotionLevel())
                .toString();
    }

    public Point getRandomPoint() {
        return randomPointsBuilder.getGeometry().getInteriorPoint();
    }

    public abstract Coordinate[] getCoordinates();

    public abstract EmotionType getEmotionType();

    public abstract EmotionLevel getEmotionLevel();

}
