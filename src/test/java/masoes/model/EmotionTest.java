/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class EmotionTest {

    private Coordinate[] coordinates;
    private Emotion emotionSpy;

    @Before
    public void setUp() {
        coordinates = new Coordinate[]{
                new Coordinate(0, 0.5),
                new Coordinate(0, 1),
                new Coordinate(-1, 1),
                new Coordinate(-1, 0),
                new Coordinate(-0.5, 0),
                new Coordinate(-0.5, 0.5),
                new Coordinate(0, 0.5)
        };
        emotionSpy = spy(createDummyEmotion(coordinates));
    }

    @Test
    public void shouldCreateGeometryPolygonWithCoordinates() {
        Polygon expectedPolygon = new GeometryFactory().createPolygon(coordinates);
        assertReflectionEquals(expectedPolygon.getCoordinates(), emotionSpy.getGeometry().getCoordinates());
    }

    @Test
    public void shouldGetRandomPointInsideGeometry() {
        IntStream.range(0, 1000).forEach(i -> {
            Point pointA = emotionSpy.getRandomPoint();
            Point pointB = emotionSpy.getRandomPoint();
            assertTrue(emotionSpy.getGeometry().intersects(pointA));
            assertTrue(emotionSpy.getGeometry().intersects(pointB));
            assertThat(pointA.getX(), is(not(pointB.getX())));
            assertThat(pointA.getY(), is(not(pointB.getY())));
        });
    }

    private Emotion createDummyEmotion(Coordinate[] coordinates) {
        return new Emotion() {
            @Override
            public Coordinate[] getCoordinates() {
                return coordinates;
            }

            @Override
            public EmotionType getEmotionType() {
                return null;
            }

            @Override
            public EmotionLevel getEmotionLevel() {
                return null;
            }
        };
    }

}

