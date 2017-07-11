/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import knowledge.Knowledge;
import masoes.component.behavioural.BehaviouralComponent;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DummyEmotionalAgentTest extends PowerMockitoTest {

    private DummyEmotionalAgent dummyEmotionalAgentSpy;
    private BehaviouralComponent behaviouralComponentMock;

    @Before
    public void setUp() {
        dummyEmotionalAgentSpy = spy(DummyEmotionalAgent.class);
        behaviouralComponentMock = mock(BehaviouralComponent.class);
        doReturn(behaviouralComponentMock).when(dummyEmotionalAgentSpy).getBehaviouralComponent();
    }

    @Test
    public void shouldAddKnowledge() {
        dummyEmotionalAgentSpy.setUp();
        verify(behaviouralComponentMock).addKnowledge(any(Knowledge.class));
    }

}