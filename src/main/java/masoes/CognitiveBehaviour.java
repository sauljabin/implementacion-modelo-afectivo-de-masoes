/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import masoes.behavioural.BehaviourType;

public abstract class CognitiveBehaviour extends EmotionalBehaviour {

    @Override
    public final BehaviourType getType() {
        return BehaviourType.COGNITIVE;
    }

}
