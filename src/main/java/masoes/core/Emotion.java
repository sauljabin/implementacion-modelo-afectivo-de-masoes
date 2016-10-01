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

    private GeometryFactory geometryFactory;

    public Emotion() {
        geometryFactory = new GeometryFactory();
    }

    public Geometry getGeometry() {
        return geometryFactory.createPolygon(getCoordinates());
    }

    public abstract Coordinate[] getCoordinates();

    public abstract EmotionType getEmotionType();

}
