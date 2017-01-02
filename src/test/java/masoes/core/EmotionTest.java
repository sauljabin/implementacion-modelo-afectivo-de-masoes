/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class EmotionTest {

    private Coordinate[] coordinates;
    private Emotion emotion;

    @Before
    public void setUp() throws Exception {
        coordinates = new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0)};
        emotion = createDummyEmotion(null, coordinates);
    }

    @Test
    public void shouldInvokeNameWhenToString() {
        String expectedEmotionName = "EmotionName";
        Emotion emotion = createDummyEmotion(expectedEmotionName, null);
        assertThat(emotion.toString(), is(expectedEmotionName));
    }

    @Test
    public void shouldReturnPolygon() {
        Polygon expectedPolygon = new GeometryFactory().createPolygon(coordinates);
        assertReflectionEquals(expectedPolygon.getCoordinates(), emotion.getGeometry().getCoordinates());
    }

    @Test
    public void shouldGetRandomPointInGeometry() {
        IntStream.range(0, 1000).forEach(i -> {
            Point point = emotion.getRandomPoint();
            assertTrue(emotion.getGeometry().intersects(point));
        });
    }

    private Emotion createDummyEmotion(String name, Coordinate[] coordinates) {
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
            public String getName() {
                return name;
            }
        };
    }

}

