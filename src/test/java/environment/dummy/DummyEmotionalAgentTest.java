/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import environment.dummy.DummyCognitiveBehaviour;
import environment.dummy.DummyEmotionalAgent;
import environment.dummy.DummyImitativeBehaviour;
import environment.dummy.DummyReactiveBehaviour;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DummyEmotionalAgentTest {

    private DummyEmotionalAgent dummyEmotionalAgentSpy;

    @Before
    public void setUp() {
        dummyEmotionalAgentSpy = Mockito.spy(new DummyEmotionalAgent());
    }

    @Test
    public void shouldSetCorrectBehaviourConfiguration() {
        dummyEmotionalAgentSpy.setUp();
        verify(dummyEmotionalAgentSpy).setCognitiveBehaviour(isA(DummyCognitiveBehaviour.class));
        verify(dummyEmotionalAgentSpy).setImitativeBehaviour(isA(DummyImitativeBehaviour.class));
        verify(dummyEmotionalAgentSpy).setReactiveBehaviour(isA(DummyReactiveBehaviour.class));
    }

}