/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class EmotionTest {

    @Test
    public void shouldInvokeNameWhenToString() {
        String expectedEmotionName = "EmotionName";
        Emotion emotion = createDummyEmotion(expectedEmotionName, null);
        assertThat(emotion.toString(), is(expectedEmotionName));
    }

    @Test
    public void shouldReturnPolygon() {
        Coordinate[] coordinates = {new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0)};
        Emotion emotion = createDummyEmotion(null, coordinates);
        Polygon expectedPolygon = new GeometryFactory().createPolygon(coordinates);
        assertReflectionEquals(expectedPolygon, emotion.getGeometry());
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

