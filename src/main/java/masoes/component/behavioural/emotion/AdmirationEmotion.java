/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural.emotion;

import com.vividsolutions.jts.geom.Coordinate;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionLevel;
import masoes.component.behavioural.EmotionType;

public class AdmirationEmotion extends Emotion {

    @Override
    public String getName() {
        return "admiration";
    }

    @Override
    public Coordinate[] getCoordinates() {
        return new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, 0.5),
                new Coordinate(-0.5, 0.5),
                new Coordinate(-0.5, 0),
                new Coordinate(0, 0)
        };
    }

    @Override
    public EmotionType getType() {
        return EmotionType.POSITIVE;
    }

    @Override
    public EmotionLevel getLevel() {
        return EmotionLevel.COLLECTIVE;
    }

}
