/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.environment.dummy;

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
    public void shouldSetCorrectBehaviourConfiguration() {
        spyDummyEmotionalAgent.setUp();
        verify(spyDummyEmotionalAgent).setCognitiveBehaviour(any(DummyCognitiveBehaviour.class));
        verify(spyDummyEmotionalAgent).setImitativeBehaviour(any(DummyImitativeBehaviour.class));
        verify(spyDummyEmotionalAgent).setReactiveBehaviour(any(DummyReactiveBehaviour.class));
    }

}