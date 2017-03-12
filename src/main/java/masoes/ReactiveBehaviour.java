/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import masoes.behavioural.BehaviourType;

public abstract class ReactiveBehaviour extends EmotionalBehaviour {

    @Override
    public BehaviourType getType() {
        return BehaviourType.REACTIVE;
    }

}
