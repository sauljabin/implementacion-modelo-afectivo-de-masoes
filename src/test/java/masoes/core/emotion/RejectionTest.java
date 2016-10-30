/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import masoes.core.EmotionType;
import masoes.util.math.GeometryCreator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class RejectionTest {

    private Rejection rejection;
    private Coordinate[] coordinates;
    private GeometryCreator geometryCreator;

    @Before
    public void setUp() {
        geometryCreator = new GeometryCreator();
        rejection = new Rejection();
        coordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, -0.5),
                new Coordinate(0.5, -0.5),
                new Coordinate(0.5, 0),
                new Coordinate(0, 0)
        };
    }

    @Test
    public void shouldIntersectsWithBoundaryPoints() {
        Arrays.stream(coordinates)
                .forEach(coordinate -> assertTrue(rejection.getGeometry().intersects(geometryCreator.createPoint(coordinate.x, coordinate.y))));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(rejection.getGeometry().intersects(geometryCreator.createPoint(0.7, 0.7)));
        assertFalse(rejection.getGeometry().intersects(geometryCreator.createPoint(0.51, 0.51)));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(rejection.getGeometry().intersects(geometryCreator.createPoint(0.1, -0.1)));
        assertTrue(rejection.getGeometry().intersects(geometryCreator.createPoint(0.4, -0.4)));
    }

    @Test
    public void shouldReturnCorrectConfiguration() {
        assertReflectionEquals(rejection.getGeometry(), geometryCreator.createPolygon(coordinates));
        assertThat(rejection.getName(), is("Rejection"));
        assertThat(rejection.getEmotionType(), is(EmotionType.NEGATIVE_LOW));
    }

}
