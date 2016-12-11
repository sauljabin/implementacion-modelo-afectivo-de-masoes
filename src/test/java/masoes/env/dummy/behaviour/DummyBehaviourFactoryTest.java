/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy.behaviour;

import masoes.core.EmotionalAgent;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

public class DummyBehaviourFactoryTest {

    private DummyBehaviourFactory dummyBehaviourFactory;
    private EmotionalAgent mockEmotionalAgent;

    @Before
    public void setUp() {
        dummyBehaviourFactory = new DummyBehaviourFactory();
        mockEmotionalAgent = mock(EmotionalAgent.class);
    }

    @Test
    public void shouldReturnCognitiveBehaviour() {
        assertThat(dummyBehaviourFactory.createCognitiveBehaviour(mockEmotionalAgent), is(instanceOf(DummyCognitiveBehaviour.class)));
    }

    @Test
    public void shouldReturnImitativeBehaviour() {
        assertThat(dummyBehaviourFactory.createImitativeBehaviour(mockEmotionalAgent), is(instanceOf(DummyImitativeBehaviour.class)));
    }

    @Test
    public void shouldReturnReactiveBehaviour() {
        assertThat(dummyBehaviourFactory.createReactiveBehaviour(mockEmotionalAgent), is(instanceOf(DummyReactiveBehaviour.class)));
    }

}