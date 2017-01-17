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

public class RejectionEmotion extends Emotion {

    @Override
    public Coordinate[] getCoordinates() {
        return new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, -0.5),
                new Coordinate(0.5, -0.5),
                new Coordinate(0.5, 0),
                new Coordinate(0, 0)
        };
    }

    @Override
    public EmotionType getEmotionType() {
        return EmotionType.NEGATIVE_LOW;
    }

    @Override
    public EmotionLevel getEmotionLevel() {
        return EmotionLevel.COLLECTIVE;
    }

}
