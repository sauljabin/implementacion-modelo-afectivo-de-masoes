/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import masoes.core.EmotionType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CompassionTest {

    private Compassion compassion;
    private GeometryFactory geometryFactory;
    private Coordinate[] coordinates;

    @Before
    public void setUp() {
        geometryFactory = new GeometryFactory();
        compassion = new Compassion();
        coordinates = new Coordinate[]{
                new Coordinate(0, 0.5),
                new Coordinate(0, 1),
                new Coordinate(-1, 1),
                new Coordinate(-1, 0),
                new Coordinate(-0.5, 0),
                new Coordinate(-0.5, 0.5),
                new Coordinate(0, 0.5)
        };
    }

    @Test
    public void shouldIntersectsWithBoundaryPoints() {
        for (Coordinate coordinate : coordinates) {
            assertTrue(compassion.getGeometry().intersects(geometryFactory.createPoint(coordinate)));
        }
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(compassion.getGeometry().intersects(createPoint(-0.7, 0.7)));
        assertTrue(compassion.getGeometry().intersects(createPoint(-0.51, 0.51)));
    }

    public Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(compassion.getGeometry().intersects(createPoint(-0.1, 0.1)));
        assertFalse(compassion.getGeometry().intersects(createPoint(-1.1, 0.1)));
    }

    @Test
    public void shouldReturnCorrectEmotionType() {
        assertThat(compassion.getEmotionType(), is(EmotionType.POSITIVE));
    }

}
