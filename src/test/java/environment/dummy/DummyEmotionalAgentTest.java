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

import java.nio.file.Paths;
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

    private static final String THEORY = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";

    private ArgumentCaptor<EmotionalState> emotionalStateArgumentCaptor;
    private ArgumentCaptor<Knowledge> knowledgeArgumentCaptor;

    private DummyEmotionalAgent dummyEmotionalAgentSpy;
    private BehaviouralComponent behaviouralComponentMock;

    @Before
    public void setUp() {
        emotionalStateArgumentCaptor = ArgumentCaptor.forClass(EmotionalState.class);
        knowledgeArgumentCaptor = ArgumentCaptor.forClass(Knowledge.class);
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

        Object[] args = {"--activation=" + activationString, "--satisfaction=" + satisfactionString};

        dummyEmotionalAgentSpy.setArguments(args);
        dummyEmotionalAgentSpy.setUp();

        verify(behaviouralComponentMock).setEmotionalState(emotionalStateArgumentCaptor.capture());

        EmotionalState emotionalState = emotionalStateArgumentCaptor.getValue();

        assertThat(emotionalState.getActivation(), is(activation));
        assertThat(emotionalState.getSatisfaction(), is(satisfaction));
    }

    @Test
    public void shouldSetKnowledgePathWhenReceivePath() {
        Object[] args = {"-kp", THEORY};
        dummyEmotionalAgentSpy.setArguments(args);
        dummyEmotionalAgentSpy.setUp();

        verify(behaviouralComponentMock).addKnowledge(knowledgeArgumentCaptor.capture());

        Knowledge knowledge = knowledgeArgumentCaptor.getValue();
        assertThat(knowledge.toString(), is(new Knowledge(Paths.get(THEORY)).toString().trim()));
    }

    @Test
    public void shouldSetKnowledgePathWhenReceivePathAndLongOption() {
        Object[] args = {"--knowledge-path=" + THEORY};
        dummyEmotionalAgentSpy.setArguments(args);
        dummyEmotionalAgentSpy.setUp();

        verify(behaviouralComponentMock).addKnowledge(knowledgeArgumentCaptor.capture());

        Knowledge knowledge = knowledgeArgumentCaptor.getValue();
        assertThat(knowledge.toString(), is(new Knowledge(Paths.get(THEORY)).toString().trim()));
    }

    @Test
    public void shouldSetKnowledge() {
        String expectedRule = "stimulus(AGENT, eventName, 0.1, 0.1) :- self(AGENT).";
        Object[] args = {"-k", expectedRule};
        dummyEmotionalAgentSpy.setArguments(args);
        dummyEmotionalAgentSpy.setUp();

        verify(behaviouralComponentMock).addKnowledge(knowledgeArgumentCaptor.capture());

        Knowledge knowledge = knowledgeArgumentCaptor.getValue();
        assertThat(knowledge.toString(), is(new Knowledge(expectedRule).toString().trim()));
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