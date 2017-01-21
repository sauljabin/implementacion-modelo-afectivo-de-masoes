/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import jade.core.behaviours.Behaviour;
import logger.jade.JadeLogger;
import masoes.behaviour.ResponseAgentStatusBehaviour;
import masoes.behaviour.StimulusReceiverBehaviour;
import masoes.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class EmotionalAgentTest {

    private EmotionalState emotionalStateMock;
    private EmotionalAgent spyEmotionalAgent;
    private BehaviourManager behaviourManagerMock;
    private Behaviour behaviourMock;
    private EmotionalConfigurator emotionalConfiguratorMock;
    private Emotion emotionMock;
    private Stimulus stimulusMock;
    private JadeLogger loggerMock;
    private ArgumentCaptor<Behaviour> behaviourArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        behaviourArgumentCaptor = ArgumentCaptor.forClass(Behaviour.class);

        behaviourManagerMock = mock(BehaviourManager.class);
        emotionalConfiguratorMock = mock(EmotionalConfigurator.class);
        loggerMock = mock(JadeLogger.class);
        emotionalStateMock = mock(EmotionalState.class);
        behaviourMock = mock(Behaviour.class);
        emotionMock = mock(Emotion.class);
        stimulusMock = mock(Stimulus.class);

        spyEmotionalAgent = createAgent();

        doReturn(emotionMock).when(emotionalConfiguratorMock).getEmotion();
        doReturn(emotionalStateMock).when(emotionalConfiguratorMock).getEmotionalState();
        doReturn(behaviourMock).when(behaviourManagerMock).calculateBehaviour(any());
        doReturn(behaviourMock).when(behaviourManagerMock).getBehaviour();
        doNothing().when(behaviourManagerMock).updateBehaviour(any());

        spyEmotionalAgent.setup();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(behaviourArgumentCaptor.capture());
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(behaviourArgumentCaptor.capture());
        assertThat(spyEmotionalAgent.getCurrentEmotionalBehaviour(), is(behaviourMock));
        assertThat(spyEmotionalAgent.getCurrentEmotion(), is(emotionMock));
        assertThat(behaviourArgumentCaptor.getAllValues(), hasItems(is(instanceOf(ResponseAgentStatusBehaviour.class)), is(instanceOf(StimulusReceiverBehaviour.class))));

        InOrder inOrder = inOrder(spyEmotionalAgent, behaviourManagerMock);
        inOrder.verify(spyEmotionalAgent).setUp();
        inOrder.verify(behaviourManagerMock).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldUpdateBehaviour() throws Exception {
        spyEmotionalAgent = createAgent();
        spyEmotionalAgent.evaluateStimulus(stimulusMock);
        verify(behaviourManagerMock).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldUpdateEmotion() {
        spyEmotionalAgent.evaluateStimulus(stimulusMock);
        verify(emotionalConfiguratorMock).updateEmotion(stimulusMock);
    }

    @Test
    public void shouldLogEmotionChange() {
        spyEmotionalAgent.evaluateStimulus(stimulusMock);
        verify(loggerMock).agentEmotionalState(spyEmotionalAgent);
        verify(loggerMock).agentEmotionalStateChanged(spyEmotionalAgent, stimulusMock);
    }

    private EmotionalAgent createAgent() throws Exception {
        EmotionalAgent emotionalAgent = new EmotionalAgent();
        setFieldValue(emotionalAgent, "behaviourManager", behaviourManagerMock);
        setFieldValue(emotionalAgent, "emotionalConfigurator", emotionalConfiguratorMock);
        setFieldValue(emotionalAgent, "logger", loggerMock);
        return spy(emotionalAgent);
    }

}