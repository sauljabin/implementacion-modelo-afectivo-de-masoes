/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.core.behaviours.Behaviour;
import logger.writer.JadeLogger;
import masoes.model.BehaviourManager;
import masoes.model.Emotion;
import masoes.model.EmotionalConfigurator;
import masoes.model.EmotionalState;
import masoes.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
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

    @Before
    public void setUp() throws Exception {
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
        doReturn(behaviourMock).when(behaviourManagerMock).getBehaviour();
        doNothing().when(behaviourManagerMock).updateBehaviour(any());

        spyEmotionalAgent.setup();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        verify(spyEmotionalAgent).addBehaviour(isA(ResponseAgentStatusBehaviour.class));
        verify(spyEmotionalAgent).addBehaviour(isA(StimulusReceiverBehaviour.class));
        assertThat(spyEmotionalAgent.getCurrentEmotionalBehaviour(), is(behaviourMock));
        assertThat(spyEmotionalAgent.getCurrentEmotion(), is(emotionMock));

        InOrder inOrder = inOrder(spyEmotionalAgent, behaviourManagerMock);
        inOrder.verify(spyEmotionalAgent).setUp();
        inOrder.verify(behaviourManagerMock).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldUpdateBehaviourWhenEvaluateStimulus() throws Exception {
        spyEmotionalAgent = createAgent();
        spyEmotionalAgent.evaluateStimulus(stimulusMock);
        verify(behaviourManagerMock).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldUpdateEmotionWhenEvaluateStimulus() {
        spyEmotionalAgent.evaluateStimulus(stimulusMock);
        verify(emotionalConfiguratorMock).updateEmotion(stimulusMock);
    }

    @Test
    public void shouldLogEmotionChange() {
        spyEmotionalAgent.evaluateStimulus(stimulusMock);
        verify(loggerMock, times(2)).agent(spyEmotionalAgent);
    }

    private EmotionalAgent createAgent() throws Exception {
        EmotionalAgent emotionalAgent = new EmotionalAgent();
        setFieldValue(emotionalAgent, "behaviourManager", behaviourManagerMock);
        setFieldValue(emotionalAgent, "emotionalConfigurator", emotionalConfiguratorMock);
        setFieldValue(emotionalAgent, "logger", loggerMock);
        return spy(emotionalAgent);
    }

}