/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy.behaviour;

import jade.core.behaviours.Behaviour;
import masoes.core.BehaviourFactory;
import masoes.core.EmotionalAgent;

public class DummyBehaviourFactory extends BehaviourFactory {

    @Override
    public Behaviour createReactiveBehaviour(EmotionalAgent agent) {
        return new DummyReactiveBehaviour();
    }

    @Override
    public Behaviour createImitativeBehaviour(EmotionalAgent agent) {
        return new DummyImitativeBehaviour();
    }

    @Override
    public Behaviour createCognitiveBehaviour(EmotionalAgent agent) {
        return new DummyCognitiveBehaviour();
    }

}
