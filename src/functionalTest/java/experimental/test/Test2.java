/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental.test;

import experimental.FunctionalTest;

public class Test2 extends FunctionalTest {

    @Override
    public void setUp() {
        System.out.println("========================> " + this.getClass().getCanonicalName());
    }

    @Override
    public void action() {

    }
}
