/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public abstract class Emotion {

    private Geometry polygon;
    private GeometryFactory geometryFactory;

    public Emotion() {
        geometryFactory = new GeometryFactory();
        polygon = geometryFactory.createPolygon(getCoordinates());
    }

    public Geometry getGeometry() {
        return polygon;
    }

    public abstract Coordinate[] getCoordinates();

    public abstract EmotionType getEmotionType();

    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }

}
