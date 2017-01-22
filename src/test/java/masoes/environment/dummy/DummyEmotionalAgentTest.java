/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.environment.dummy;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
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
        verify(dummyEmotionalAgentSpy).setCognitiveBehaviour(isA(DummyCognitiveBehaviour.class));
        verify(dummyEmotionalAgentSpy).setImitativeBehaviour(isA(DummyImitativeBehaviour.class));
        verify(dummyEmotionalAgentSpy).setReactiveBehaviour(isA(DummyReactiveBehaviour.class));
    }

}