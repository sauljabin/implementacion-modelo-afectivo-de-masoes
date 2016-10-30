/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.Agent;

public abstract class EmotionalAgent extends Agent {

    protected EmotionalModel emotionalModel;

    public EmotionalModel getEmotionalModel() {
        return emotionalModel;
    }

}
