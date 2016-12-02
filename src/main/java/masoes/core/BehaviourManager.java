/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;

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

    public void updateBehaviour(Emotion emotion) {
        behaviour = calculateBehaviour(emotion);
    }

    protected Behaviour calculateBehaviour(Emotion emotion) {
        BehaviourType behaviourType = getBehaviourTypeAssociated(emotion.getEmotionType());
        switch (behaviourType) {
            case COGNITIVE:
                return createCognitiveBehaviour();
            case IMITATIVE:
                return createImitativeBehaviour();
            case REACTIVE:
            default:
                return createReactiveBehaviour();
        }
    }

    protected Behaviour createReactiveBehaviour() {
        return null;
    }

    protected Behaviour createImitativeBehaviour() {
        return null;
    }

    protected Behaviour createCognitiveBehaviour() {
        return null;
    }

}
