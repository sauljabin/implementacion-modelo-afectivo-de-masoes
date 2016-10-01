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

public class SadnessTest {

    private Sadness sadness;
    private GeometryFactory geometryFactory;
    private Coordinate[] coordinates;

    @Before
    public void setUp() {
        geometryFactory = new GeometryFactory();
        sadness = new Sadness();
        coordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, -0.5),
                new Coordinate(-0.5, -0.5),
                new Coordinate(-0.5, 0),
                new Coordinate(0, 0)
        };
    }

    @Test
    public void shouldIntersectsWithBoundaryPoints() {
        Arrays.stream(coordinates)
                .forEach(coordinate -> assertTrue(sadness.getGeometry().intersects(geometryFactory.createPoint(coordinate))));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(sadness.getGeometry().intersects(createPoint(-0.1, -0.1)));
        assertTrue(sadness.getGeometry().intersects(createPoint(-0.4, -0.4)));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(sadness.getGeometry().intersects(createPoint(0.1, -0.1)));
        assertFalse(sadness.getGeometry().intersects(createPoint(1.1, -0.1)));
        assertFalse(sadness.getGeometry().intersects(createPoint(-0.6, -0.6)));
    }

    @Test
    public void shouldReturnCorrectEmotionType() {
        assertThat(sadness.getEmotionType(), is(EmotionType.NEGATIVE_LOW));
    }

    public Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

}
