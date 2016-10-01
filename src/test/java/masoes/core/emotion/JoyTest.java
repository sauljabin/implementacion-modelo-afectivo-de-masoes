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

public class JoyTest {

    private Joy joy;
    private GeometryFactory geometryFactory;
    private Coordinate[] coordinates;

    @Before
    public void setUp() {
        geometryFactory = new GeometryFactory();
        joy = new Joy();
        coordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, 0.5),
                new Coordinate(0.5, 0.5),
                new Coordinate(0.5, 0),
                new Coordinate(0, 0)
        };
    }

    @Test
    public void shouldIntersectsWithBoundaryPoints() {
        Arrays.stream(coordinates)
                .forEach(coordinate -> assertTrue(joy.getGeometry().intersects(geometryFactory.createPoint(coordinate))));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(joy.getGeometry().intersects(createPoint(0.7, 0.7)));
        assertFalse(joy.getGeometry().intersects(createPoint(0.51, 0.51)));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(joy.getGeometry().intersects(createPoint(0.1, 0.1)));
        assertTrue(joy.getGeometry().intersects(createPoint(0.4, 0.4)));
    }

    @Test
    public void shouldReturnCorrectEmotionType() {
        assertThat(joy.getEmotionType(), is(EmotionType.POSITIVE));
    }

    public Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}
