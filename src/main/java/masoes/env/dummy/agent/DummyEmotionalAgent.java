/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy.agent;

import masoes.core.EmotionalAgent;
import masoes.env.dummy.behaviour.DummyBehaviourFactory;

public class DummyEmotionalAgent extends EmotionalAgent {

    @Override
    protected void setUp() {
        setBehaviourFactory(new DummyBehaviourFactory());
    }

}
