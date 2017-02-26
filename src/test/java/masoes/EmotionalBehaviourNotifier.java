/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import masoes.behavioural.BehaviourType;

public class EmotionalBehaviourNotifier extends EmotionalBehaviour {

    @Override
    public String getName() {
        return "emotionalBehaviourForTest";
    }

    @Override
    public BehaviourType getType() {
        return null;
    }

    @Override
    public void action() {
        notifyAction("expectedActionForTest");
    }

    @Override
    public boolean done() {
        return true;
    }

}
