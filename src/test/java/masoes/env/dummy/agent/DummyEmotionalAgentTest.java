/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy.agent;

import masoes.env.dummy.behaviour.DummyBehaviourFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DummyEmotionalAgentTest {

    private DummyEmotionalAgent spyDummyEmotionalAgent;

    @Before
    public void setUp() {
        spyDummyEmotionalAgent = spy(new DummyEmotionalAgent());
    }

    @Test
    public void shouldSetCorrectBehaviourFactory() {
        spyDummyEmotionalAgent.setUp();
        verify(spyDummyEmotionalAgent).setBehaviourFactory(any(DummyBehaviourFactory.class));
    }

}