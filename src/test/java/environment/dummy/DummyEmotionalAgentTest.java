/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import knowledge.Knowledge;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.component.behavioural.EmotionalState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import test.PowerMockitoTest;

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DummyEmotionalAgentTest extends PowerMockitoTest {

    private ArgumentCaptor<EmotionalState> emotionalStateArgumentCaptor;

    private DummyEmotionalAgent dummyEmotionalAgentSpy;
    private BehaviouralComponent behaviouralComponentMock;

    @Before
    public void setUp() {
        emotionalStateArgumentCaptor = ArgumentCaptor.forClass(EmotionalState.class);
        dummyEmotionalAgentSpy = spy(DummyEmotionalAgent.class);
        behaviouralComponentMock = mock(BehaviouralComponent.class);
        doReturn(behaviouralComponentMock).when(dummyEmotionalAgentSpy).getBehaviouralComponent();
    }

    @Test
    public void shouldSetEmotionWhenReceiveArguments() {
        Random random = new Random();

        double activation = random.nextDouble();
        double satisfaction = random.nextDouble();

        String activationString = String.valueOf(activation);
        String satisfactionString = String.valueOf(satisfaction);

        Object[] args = {activationString, satisfactionString};

        dummyEmotionalAgentSpy.setArguments(args);
        dummyEmotionalAgentSpy.setUp();

        verify(behaviouralComponentMock).setEmotionalState(emotionalStateArgumentCaptor.capture());

        EmotionalState emotionalState = emotionalStateArgumentCaptor.getValue();

        assertThat(emotionalState.getActivation(), is(activation));
        assertThat(emotionalState.getSatisfaction(), is(satisfaction));
    }

    @Test
    public void shouldNotSetEmotionalStateWhenArgumentsAreInvalid() {
        Object[] args = {"TEXT", "TEXT"};
        dummyEmotionalAgentSpy.setArguments(args);
        dummyEmotionalAgentSpy.setUp();
        verify(behaviouralComponentMock, never()).setEmotionalState(any(EmotionalState.class));
    }

    @Test
    public void shouldNotSetEmotionalStateWhenArgumentsAreNull() {
        dummyEmotionalAgentSpy.setUp();
        verify(behaviouralComponentMock, never()).setEmotionalState(any(EmotionalState.class));
    }

    @Test
    public void shouldAddKnowledge() {
        dummyEmotionalAgentSpy.setUp();
        verify(behaviouralComponentMock).addKnowledge(any(Knowledge.class));
    }

}