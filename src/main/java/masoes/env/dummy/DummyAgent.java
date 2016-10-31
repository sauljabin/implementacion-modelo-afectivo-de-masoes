/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.core.EmotionalAgent;

public class DummyAgent extends EmotionalAgent {

    @Override
    protected void setup() {
        emotionalModel = new DummyEmotionalModel();
    }
}
