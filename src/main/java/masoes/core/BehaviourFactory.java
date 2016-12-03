/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;

public abstract class BehaviourFactory {

    public abstract Behaviour createReactiveBehaviour(EmotionalAgent agent);

    public abstract Behaviour createImitativeBehaviour(EmotionalAgent agent);

    public abstract Behaviour createCognitiveBehaviour(EmotionalAgent agent);

}
