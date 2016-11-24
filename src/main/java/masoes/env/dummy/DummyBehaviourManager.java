/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import jade.core.behaviours.Behaviour;
import masoes.core.BehaviourManager;
import masoes.core.BehaviourType;
import masoes.core.Emotion;
import masoes.env.dummy.behaviour.DummyCognitiveBehaviour;
import masoes.env.dummy.behaviour.DummyImitativeBehaviour;
import masoes.env.dummy.behaviour.DummyReactiveBehaviour;

public class DummyBehaviourManager extends BehaviourManager {

    @Override
    protected Behaviour calculateBehaviour(Emotion emotion) {
        BehaviourType behaviourType = getBehaviourTypeAssociated(emotion.getEmotionType());
        switch (behaviourType) {
            case COGNITIVE:
                return new DummyCognitiveBehaviour();
            case IMITATIVE:
                return new DummyImitativeBehaviour();
            case REACTIVE:
            default:
                return new DummyReactiveBehaviour();
        }
    }

}
