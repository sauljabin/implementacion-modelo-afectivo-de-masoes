/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import jade.core.behaviours.Behaviour;
import masoes.core.BehaviourManager;
import masoes.core.Emotion;

public class DummyBehaviourManager extends BehaviourManager {

    @Override
    protected Behaviour evaluateEmotion(Emotion emotion) {
        return new DummyBehaviour();
    }
}
