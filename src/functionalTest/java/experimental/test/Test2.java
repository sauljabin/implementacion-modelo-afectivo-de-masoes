/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental.test;

import experimental.FunctionalTest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Test2 extends FunctionalTest {

    @Override
    public void setUp() {
    }

    @Override
    public void performTest() {
        assertThat("x", is("y"));
    }

}
