/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.math;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.Test;

import static masoes.util.math.GeometryCreator.createPoint;
import static masoes.util.math.GeometryCreator.createPolygon;
import static masoes.util.math.RandomGenerator.random;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class GeometryCreatorTest {

    @Test
    public void shouldReturnCorrectPoint() {
        double expectedX = random(0, 10);
        double expectedY = random(0, 10);
        Point actualPoint = createPoint(expectedX, expectedY);
        assertThat(actualPoint.getX(), is(expectedX));
        assertThat(actualPoint.getY(), is(expectedY));
    }

    @Test
    public void shouldCreateCorrectPolygon() {
        Coordinate[] expectedCoordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, 0.5),
                new Coordinate(-0.5, 0.5),
                new Coordinate(-0.5, 0),
                new Coordinate(0, 0)
        };
        assertReflectionEquals(createPolygon(expectedCoordinates), new GeometryFactory().createPolygon(expectedCoordinates));
    }
}