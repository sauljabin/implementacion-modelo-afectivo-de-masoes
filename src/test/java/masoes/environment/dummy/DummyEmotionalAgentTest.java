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

    private DummyEmotionalAgent dummyEmotionalAgentSpy;

    @Before
    public void setUp() {
        dummyEmotionalAgentSpy = spy(new DummyEmotionalAgent());
    }

    @Test
    public void shouldSetCorrectBehaviourConfiguration() {
        dummyEmotionalAgentSpy.setUp();
        verify(dummyEmotionalAgentSpy).setCognitiveBehaviour(any(DummyCognitiveBehaviour.class));
        verify(dummyEmotionalAgentSpy).setImitativeBehaviour(any(DummyImitativeBehaviour.class));
        verify(dummyEmotionalAgentSpy).setReactiveBehaviour(any(DummyReactiveBehaviour.class));
    }

}