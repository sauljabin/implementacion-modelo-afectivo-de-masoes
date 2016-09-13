/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.core.BehaviorManager;
import masoes.core.BehaviorType;
import masoes.core.EmotionType;

public class MasoesBehaviorManager implements BehaviorManager {

    @Override
    public BehaviorType getBehaviorAssociated(EmotionType emotionType) {
        switch (emotionType) {
            case POSITIVE:
                return BehaviorType.IMITATIVE;
            case NEGATIVE_LOW:
                return BehaviorType.COGNITIVE;
            case NEGATIVE_HIGH:
                return BehaviorType.REACTIVE;
            default:
                return BehaviorType.IMITATIVE;
        }
    }
}
