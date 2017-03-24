/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import knowledge.KnowledgeException;
import masoes.agent.EmotionalAgent;
import masoes.component.behavioural.emotion.SadnessEmotion;
import masoes.ontology.stimulus.ActionStimulus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import test.PowerMockitoTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class BehaviouralComponentTest extends PowerMockitoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private BehaviouralComponent behaviouralComponent;

    @Before
    public void setUp() {
        EmotionalAgent emotionalAgentMock = mock(EmotionalAgent.class);
        String agentName = "agentName";
        doReturn(agentName).when(emotionalAgentMock).getLocalName();
        behaviouralComponent = new BehaviouralComponent(emotionalAgentMock);
    }

    @Test
    public void shouldThrowExceptionIfAgentNotHaveName() {
        expectedException.expect(KnowledgeException.class);
        expectedException.expectMessage("No agent name, create component in setup agent method");
        EmotionalAgent emotionalAgent = mock(EmotionalAgent.class);
        new BehaviouralComponent(emotionalAgent);
    }

    @Test
    public void shouldUpdateBehaviourAndEmotion() throws Exception {
        BehaviourManager behaviourManagerMock = mock(BehaviourManager.class);
        setFieldValue(behaviouralComponent, "behaviourManager", behaviourManagerMock);

        doReturn(BehaviourType.IMITATIVE).when(behaviourManagerMock).getBehaviourType();

        EmotionalConfigurator emotionalConfiguratorMock = mock(EmotionalConfigurator.class);
        setFieldValue(behaviouralComponent, "emotionalConfigurator", emotionalConfiguratorMock);

        Emotion emotionMock = mock(Emotion.class);
        doReturn(emotionMock).when(emotionalConfiguratorMock).getEmotion();

        EmotionalState emotionalStateMock = mock(EmotionalState.class);
        doReturn(emotionalStateMock).when(emotionalConfiguratorMock).getEmotionalState();

        ActionStimulus stimulus = new ActionStimulus();
        behaviouralComponent.evaluateStimulus(stimulus);

        verify(emotionalConfiguratorMock).updateEmotion(stimulus);
        verify(behaviourManagerMock).updateBehaviour();

        assertThat(behaviouralComponent.getCurrentEmotion(), is(emotionMock));
        assertThat(behaviouralComponent.getCurrentBehaviourType(), is(BehaviourType.IMITATIVE));
        assertThat(behaviouralComponent.getCurrentEmotionalState(), is(emotionalStateMock));
    }

    @Test
    public void shouldUpdateBehaviourWhenSetEmotionalState() throws Exception {
        BehaviourManager behaviourManagerMock = mock(BehaviourManager.class);
        setFieldValue(behaviouralComponent, "behaviourManager", behaviourManagerMock);

        doReturn(BehaviourType.IMITATIVE).when(behaviourManagerMock).getBehaviourType();
        behaviouralComponent.setEmotionalState(new SadnessEmotion().getRandomEmotionalState());

        verify(behaviourManagerMock).updateBehaviour();
    }

}