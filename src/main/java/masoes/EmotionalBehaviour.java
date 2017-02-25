/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.core.behaviours.Behaviour;
import masoes.behavioural.BehaviourType;

public abstract class EmotionalBehaviour extends Behaviour {

    public abstract String getName();

    public abstract BehaviourType getType();

}