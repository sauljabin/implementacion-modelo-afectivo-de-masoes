/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import masoes.core.EmotionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class HappinessTest {

    private Happiness happiness;
    private Coordinate[] coordinates;
    private GeometryFactory geometryFactory;

    @Before
    public void setUp() {
        geometryFactory = new GeometryFactory();
        happiness = new Happiness();
        coordinates = new Coordinate[]{
                new Coordinate(0, 0.5),
                new Coordinate(0, 1),
                new Coordinate(1, 1),
                new Coordinate(1, 0),
                new Coordinate(0.5, 0),
                new Coordinate(0.5, 0.5),
                new Coordinate(0, 0.5)
        };
    }

    @Test
    public void shouldIntersectsWithBoundaryPoints() {
        Arrays.stream(coordinates)
                .forEach(coordinate -> assertTrue(happiness.getGeometry().intersects(geometryFactory.createPoint(coordinate))));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(happiness.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.7, 0.7))));
        assertTrue(happiness.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.51, 0.51))));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(happiness.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.1, 0.1))));
        assertFalse(happiness.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(1.1, 0.1))));
    }

    @Test
    public void shouldReturnCorrectConfiguration() {
        assertReflectionEquals(happiness.getGeometry(), geometryFactory.createPolygon(coordinates));
        assertThat(happiness.getName(), is("Happiness"));
        assertThat(happiness.getEmotionType(), is(EmotionType.POSITIVE));
    }

}
