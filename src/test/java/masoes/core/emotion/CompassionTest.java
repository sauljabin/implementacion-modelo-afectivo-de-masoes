/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import masoes.core.EmotionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static masoes.util.math.GeometryCreator.createPoint;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CompassionTest {

    private Compassion compassion;
    private Coordinate[] coordinates;

    @Before
    public void setUp() {
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
                .forEach(coordinate -> assertTrue(compassion.getGeometry().intersects(createPoint(coordinate.x, coordinate.y))));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(compassion.getGeometry().intersects(createPoint(-0.7, 0.7)));
        assertTrue(compassion.getGeometry().intersects(createPoint(-0.51, 0.51)));
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
