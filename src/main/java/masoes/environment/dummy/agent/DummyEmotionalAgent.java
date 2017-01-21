/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.environment.dummy.agent;

import masoes.core.EmotionalAgent;
import masoes.environment.dummy.behaviour.DummyCognitiveBehaviour;
import masoes.environment.dummy.behaviour.DummyImitativeBehaviour;
import masoes.environment.dummy.behaviour.DummyReactiveBehaviour;

public class DummyEmotionalAgent extends EmotionalAgent {

    @Override
    protected void setUp() {
        setCognitiveBehaviour(new DummyCognitiveBehaviour());
        setReactiveBehaviour(new DummyReactiveBehaviour());
        setImitativeBehaviour(new DummyImitativeBehaviour());
    }

}
