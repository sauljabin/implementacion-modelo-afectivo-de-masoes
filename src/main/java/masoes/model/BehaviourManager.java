/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import jade.core.behaviours.Behaviour;

import java.util.Optional;

public class BehaviourManager {

    private Behaviour behaviour;

    public Behaviour getBehaviour() {
        return behaviour;
    }

    public BehaviourType getBehaviourTypeAssociated(EmotionType emotionType) {
        switch (emotionType) {
            case POSITIVE:
                return BehaviourType.IMITATIVE;
            case NEGATIVE_LOW:
                return BehaviourType.COGNITIVE;
            case NEGATIVE_HIGH:
                return BehaviourType.REACTIVE;
            default:
                return BehaviourType.IMITATIVE;
        }
    }

    public void updateBehaviour(EmotionalAgent agent) {
        if (Optional.ofNullable(behaviour).isPresent()) {
            agent.removeBehaviour(behaviour);
        }

        behaviour = calculateBehaviour(agent);

        if (Optional.ofNullable(behaviour).isPresent()) {
            agent.addBehaviour(behaviour);
        }
    }

    protected Behaviour calculateBehaviour(EmotionalAgent agent) {
        switch (getBehaviourTypeAssociated(agent.getCurrentEmotion().getEmotionType())) {
            case COGNITIVE:
                return agent.getCognitiveBehaviour();
            case IMITATIVE:
                return agent.getImitativeBehaviour();
            case REACTIVE:
            default:
                return agent.getReactiveBehaviour();
        }
    }


}
