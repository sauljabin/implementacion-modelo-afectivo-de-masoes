/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import masoes.core.EmotionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RejectionTest {
    private Rejection rejection;
    private GeometryFactory geometryFactory;
    private Coordinate[] coordinates;

    @Before
    public void setUp() {
        geometryFactory = new GeometryFactory();
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
                .forEach(coordinate -> assertTrue(rejection.getGeometry().intersects(geometryFactory.createPoint(coordinate))));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(rejection.getGeometry().intersects(createPoint(0.7, 0.7)));
        assertFalse(rejection.getGeometry().intersects(createPoint(0.51, 0.51)));
    }

    public Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(rejection.getGeometry().intersects(createPoint(0.1, -0.1)));
        assertTrue(rejection.getGeometry().intersects(createPoint(0.4, -0.4)));
    }

    @Test
    public void shouldReturnCorrectEmotionType() {
        assertThat(rejection.getEmotionType(), is(EmotionType.NEGATIVE_LOW));
    }
}
