/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.core.EmotionalAgent;
import masoes.core.EmotionalModel;

public class DummyEmotionalAgent extends EmotionalAgent {

    @Override
    protected EmotionalModel createEmotionalModel() {
        return new EmotionalModel(new DummyEmotionalConfigurator(), new DummyBehaviourManager());
    }

    @Override
    protected void setUp() {

    }

}
