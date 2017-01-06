/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;
import jade.logger.JadeLogger;
import masoes.core.behaviour.ReplayAgentInformationBehaviour;
import masoes.core.behaviour.StimulusReceiverBehaviour;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class EmotionalAgentTest {

    private EmotionalState mockEmotionalState;
    private EmotionalAgent spyEmotionalAgent;
    private BehaviourManager mockBehaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalConfigurator mockEmotionalConfigurator;
    private Emotion mockEmotion;
    private Stimulus mockStimulus;
    private JadeLogger mockLogger;

    @Before
    public void setUp() throws Exception {
        mockBehaviourManager = mock(BehaviourManager.class);
        mockEmotionalConfigurator = mock(EmotionalConfigurator.class);
        mockLogger = mock(JadeLogger.class);

        spyEmotionalAgent = createAgent();

        mockEmotionalState = mock(EmotionalState.class);
        mockBehaviour = mock(Behaviour.class);
        mockEmotion = mock(Emotion.class);
        mockStimulus = mock(Stimulus.class);

        when(mockEmotionalConfigurator.getEmotion()).thenReturn(mockEmotion);
        when(mockEmotionalConfigurator.getEmotionalState()).thenReturn(mockEmotionalState);
        when(mockBehaviourManager.calculateBehaviour(any())).thenReturn(mockBehaviour);
        when(mockBehaviourManager.getBehaviour()).thenReturn(mockBehaviour);
        doNothing().when(mockBehaviourManager).updateBehaviour(any());

        spyEmotionalAgent.setup();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(any(ReplayAgentInformationBehaviour.class));
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(any(StimulusReceiverBehaviour.class));
        assertThat(spyEmotionalAgent.getEmotionalBehaviour(), is(mockBehaviour));
        assertThat(spyEmotionalAgent.getCurrentEmotion(), is(mockEmotion));

        InOrder inOrder = inOrder(spyEmotionalAgent, mockBehaviourManager);
        inOrder.verify(spyEmotionalAgent).setUp();
        inOrder.verify(mockBehaviourManager).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldUpdateBehaviour() throws Exception {
        spyEmotionalAgent = createAgent();
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockBehaviourManager).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldUpdateEmotion() {
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockEmotionalConfigurator).updateEmotion(mockStimulus);
    }

    @Test
    public void shouldLogEmotionChange() {
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockLogger).agentEmotionalState(spyEmotionalAgent);
        verify(mockLogger).agentEmotionalStateChanged(spyEmotionalAgent, mockStimulus);
    }

    private EmotionalAgent createAgent() throws Exception {
        EmotionalAgent emotionalAgent = new EmotionalAgent();
        setFieldValue(emotionalAgent, "behaviourManager", mockBehaviourManager);
        setFieldValue(emotionalAgent, "emotionalConfigurator", mockEmotionalConfigurator);
        setFieldValue(emotionalAgent, "logger", mockLogger);
        return spy(emotionalAgent);
    }

}