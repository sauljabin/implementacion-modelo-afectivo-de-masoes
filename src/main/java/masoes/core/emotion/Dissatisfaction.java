/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import masoes.core.Emotion;
import masoes.core.EmotionType;

public class Dissatisfaction implements Emotion {

    private Polygon polygon;
    private GeometryFactory geometryFactory;
    private Coordinate[] coordinates;

    public Dissatisfaction() {
        geometryFactory = new GeometryFactory();
        coordinates = new Coordinate[]{
                new Coordinate(0, -0.5),
                new Coordinate(0, -1),
                new Coordinate(1, -1),
                new Coordinate(1, 0),
                new Coordinate(0.5, 0),
                new Coordinate(0.5, -0.5),
                new Coordinate(0, -0.5)
        };
        polygon = geometryFactory.createPolygon(coordinates);
    }

    @Override
    public Geometry getGeometry() {
        return polygon;
    }

    @Override
    public EmotionType getEmotionType() {
        return EmotionType.NEGATIVE_HIGH;
    }

}
