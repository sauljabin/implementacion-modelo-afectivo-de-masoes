package masoes.util.math;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class GeometryGenerator {

    private GeometryGenerator() {
    }

    public static Point createPoint(double x, double y) {
        return new GeometryFactory().createPoint(new Coordinate(x, y));
    }
}