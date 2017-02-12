/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import masoes.ReactiveBehaviour;

public class DummyReactiveBehaviour extends ReactiveBehaviour {

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public String getName() {
        return "dummyReactive";
    }

}
