/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import masoes.util.math.GeometryCreator;
import org.junit.Before;
import org.junit.Test;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class EmotionTest {

    private Emotion emotion;
    private Coordinate[] expectedCoordinates;
    private Geometry expectedGeometry;
    private GeometryCreator geometryCreator;

    @Before
    public void setUp() throws Exception {
        geometryCreator = new GeometryCreator();
        expectedCoordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, 0.5),
                new Coordinate(-0.5, 0.5),
                new Coordinate(-0.5, 0),
                new Coordinate(0, 0)
        };
        emotion = createEmotion(expectedCoordinates, null);
        expectedGeometry = geometryCreator.createPolygon(expectedCoordinates);
    }

    @Test
    public void shouldGetCorrectGeometry() {
        assertReflectionEquals(expectedGeometry, emotion.getGeometry());
    }

    private Emotion createEmotion(Coordinate[] coordinates, EmotionType emotionType) {
        return new Emotion() {
            @Override
            public Coordinate[] getCoordinates() {
                return coordinates;
            }

            @Override
            public EmotionType getEmotionType() {
                return emotionType;
            }
        };
    }

}