/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import masoes.core.EmotionLevel;
import masoes.core.EmotionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class CompassionTest {

    private Compassion compassion;
    private Coordinate[] coordinates;
    private GeometryFactory geometryFactory;

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
        Arrays.stream(coordinates)
                .forEach(coordinate -> assertTrue(compassion.getGeometry().intersects(geometryFactory.createPoint(coordinate))));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(compassion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-0.7, 0.7))));
        assertTrue(compassion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-0.51, 0.51))));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(compassion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-0.1, 0.1))));
        assertFalse(compassion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-1.1, 0.1))));
    }

    @Test
    public void shouldReturnCorrectConfiguration() {
        Polygon expectedPolygon = geometryFactory.createPolygon(coordinates);
        assertReflectionEquals(expectedPolygon.getCoordinates(), compassion.getGeometry().getCoordinates());
        assertThat(compassion.getEmotionName(), is("Compassion"));
        assertThat(compassion.getEmotionLevel(), is(EmotionLevel.COLLECTIVE));
        assertThat(compassion.getEmotionType(), is(EmotionType.POSITIVE));
    }

}
