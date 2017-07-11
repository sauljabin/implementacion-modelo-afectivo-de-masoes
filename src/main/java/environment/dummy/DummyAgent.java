/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import jade.core.Agent;

public class DummyAgent extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new DummyBehaviour());
    }

}
