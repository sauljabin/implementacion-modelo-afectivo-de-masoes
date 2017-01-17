/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import masoes.core.Emotion;
import masoes.core.EmotionLevel;
import masoes.core.EmotionType;

public class AngerEmotion extends Emotion {

    @Override
    public Coordinate[] getCoordinates() {
        return new Coordinate[]{
                new Coordinate(0, -0.5),
                new Coordinate(0, -1),
                new Coordinate(1, -1),
                new Coordinate(1, 0),
                new Coordinate(0.5, 0),
                new Coordinate(0.5, -0.5),
                new Coordinate(0, -0.5)
        };
    }

    @Override
    public EmotionType getEmotionType() {
        return EmotionType.NEGATIVE_HIGH;
    }

    @Override
    public EmotionLevel getEmotionLevel() {
        return EmotionLevel.COLLECTIVE;
    }

}
