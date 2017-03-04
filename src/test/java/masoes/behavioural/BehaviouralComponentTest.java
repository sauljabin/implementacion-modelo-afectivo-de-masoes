/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import knowledge.KnowledgeException;
import masoes.EmotionalAgent;
import masoes.EmotionalBehaviour;
import ontology.masoes.ActionStimulus;
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

    @Test
    public void shouldThrowExceptionIfAgentNotHaveName() {
        expectedException.expect(KnowledgeException.class);
        expectedException.expectMessage("No agent name, create in setup agent method");
        EmotionalAgent emotionalAgent = mock(EmotionalAgent.class);
        new BehaviouralComponent(emotionalAgent);
    }

    @Test
    public void shouldUpdateBehaviourAndEmotion() throws Exception {
        EmotionalAgent emotionalAgentMock = mock(EmotionalAgent.class);
        String agentName = "agentName";
        doReturn(agentName).when(emotionalAgentMock).getLocalName();

        BehaviouralComponent behaviouralComponent = new BehaviouralComponent(emotionalAgentMock);

        BehaviourManager behaviourManagerMock = mock(BehaviourManager.class);
        setFieldValue(behaviouralComponent, "behaviourManager", behaviourManagerMock);

        EmotionalBehaviour emotionalBehaviourMock = mock(EmotionalBehaviour.class);
        doReturn(emotionalBehaviourMock).when(behaviourManagerMock).getBehaviour();

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
        assertThat(behaviouralComponent.getCurrentEmotionalBehaviour(), is(emotionalBehaviourMock));
        assertThat(behaviouralComponent.getCurrentEmotionalState(), is(emotionalStateMock));
    }

}