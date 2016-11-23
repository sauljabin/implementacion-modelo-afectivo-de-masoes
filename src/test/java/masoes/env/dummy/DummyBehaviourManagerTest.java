/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.env.dummy.behaviour.DummyBehaviour;
import org.junit.Before;
import org.junit.Test;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DummyBehaviourManagerTest {

    private DummyBehaviourManager dummyBehaviourManager;

    @Before
    public void setUp() {
        dummyBehaviourManager = new DummyBehaviourManager();
    }

    @Test
    public void shouldGetCorrectBehaviour() {
        DummyBehaviour expectedDummyBehaviour = new DummyBehaviour();
        assertReflectionEquals(expectedDummyBehaviour, dummyBehaviourManager.selectBehaviour(null));
    }

}