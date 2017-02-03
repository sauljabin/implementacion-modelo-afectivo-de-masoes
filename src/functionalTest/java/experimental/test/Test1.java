/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental.test;

import experimental.FunctionalTest;
import jade.core.Agent;

public class Test1 extends FunctionalTest {

    @Override
    public void setUp() {
        System.out.println("========================> " + this.getClass().getCanonicalName());
        createAgent("newAgent", Agent.class);

    }

    @Override
    public void performTest() {
        myAgent.blockingReceive(15000);
    }

}
