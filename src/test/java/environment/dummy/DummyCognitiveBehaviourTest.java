/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DummyCognitiveBehaviourTest {

    private DummyCognitiveBehaviour dummyCognitiveBehaviour;

    @Before
    public void setUp() {
        dummyCognitiveBehaviour = new DummyCognitiveBehaviour();
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(dummyCognitiveBehaviour.getName(), is("dummyCognitive"));
    }

}