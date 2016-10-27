/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.math;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class GeometryCreator {

    private GeometryFactory geometryFactory;

    public GeometryCreator() {
        geometryFactory = new GeometryFactory();
    }

    public Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    public Geometry createPolygon(Coordinate[] coordinates) {
        return geometryFactory.createPolygon(coordinates);
    }

}