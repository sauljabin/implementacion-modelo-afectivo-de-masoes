/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public abstract class EmotionalAgent extends Agent {

    private Behaviour currentBehaviour;

    public Behaviour getCurrentBehaviour() {
        return currentBehaviour;
    }

    public void changeCurrentBehaviour(Behaviour behaviour) {
        if (getCurrentBehaviour() != null)
            removeBehaviour(getCurrentBehaviour());

        addBehaviour(behaviour);
        currentBehaviour = behaviour;
    }

}
