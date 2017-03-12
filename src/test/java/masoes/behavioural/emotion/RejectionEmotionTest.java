/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import masoes.behavioural.EmotionLevel;
import masoes.behavioural.EmotionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class RejectionEmotionTest {

    private RejectionEmotion rejectionEmotion;
    private Coordinate[] coordinates;
    private GeometryFactory geometryFactory;

    @Before
    public void setUp() {
        geometryFactory = new GeometryFactory();
        rejectionEmotion = new RejectionEmotion();
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
                .forEach(coordinate -> assertTrue(rejectionEmotion.getGeometry().intersects(geometryFactory.createPoint(coordinate))));
    }

    @Test
    public void shouldNotContainsPoint() {
        assertFalse(rejectionEmotion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.7, 0.7))));
        assertFalse(rejectionEmotion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.51, 0.51))));
    }

    @Test
    public void shouldContainsInsidePoint() {
        assertTrue(rejectionEmotion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.1, -0.1))));
        assertTrue(rejectionEmotion.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.4, -0.4))));
    }

    @Test
    public void shouldReturnCorrectConfiguration() {
        Polygon expectedPolygon = geometryFactory.createPolygon(coordinates);
        assertReflectionEquals(expectedPolygon.getCoordinates(), rejectionEmotion.getGeometry().getCoordinates());
        assertThat(rejectionEmotion.getName(), is("rejection"));
        assertThat(rejectionEmotion.getLevel(), is(EmotionLevel.COLLECTIVE));
        assertThat(rejectionEmotion.getType(), is(EmotionType.NEGATIVE_LOW));
    }

}
