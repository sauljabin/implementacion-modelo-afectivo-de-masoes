/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;
import masoes.core.behaviour.ReplayAgentInformationBehaviour;
import masoes.core.behaviour.StimulusReceiverBehaviour;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmotionalAgentTest {

    private EmotionalAgent mockEmotionalAgent;
    private BehaviourManager mockBehaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalConfigurator mockEmotionalConfigurator;
    private Stimulus mockStimulus;
    private Emotion mockEmotion;
    private EmotionalModel emotionalModel;

    @Before
    public void setUp() {
        mockEmotionalAgent = mock(EmotionalAgent.class);
        mockBehaviourManager = mock(BehaviourManager.class);
        mockBehaviour = mock(Behaviour.class);
        mockEmotionalConfigurator = mock(EmotionalConfigurator.class);
        mockEmotion = mock(Emotion.class);
        mockStimulus = mock(Stimulus.class);
        emotionalModel = new EmotionalModel(mockEmotionalConfigurator, mockBehaviourManager);

        doCallRealMethod().when(mockEmotionalAgent).getCurrentEmotion();
        doCallRealMethod().when(mockEmotionalAgent).getEmotionalBehaviour();
        doCallRealMethod().when(mockEmotionalAgent).evaluateStimulus(any());
        doCallRealMethod().when(mockEmotionalAgent).setup();
        when(mockBehaviourManager.selectBehaviour(any())).thenReturn(mockBehaviour);
        when(mockEmotionalConfigurator.getEmotion()).thenReturn(mockEmotion);
        when(mockEmotionalAgent.createEmotionalModel()).thenReturn(emotionalModel);
        doNothing().when(mockEmotionalAgent).setUp();

        mockEmotionalAgent.setup();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        verify(mockEmotionalAgent, atLeastOnce()).addBehaviour(any(ReplayAgentInformationBehaviour.class));
        verify(mockEmotionalAgent, atLeastOnce()).addBehaviour(any(StimulusReceiverBehaviour.class));
    }

    @Test
    public void shouldChangeEmotionalBehaviour() {
        mockEmotionalAgent.evaluateStimulus(any());
        verify(mockBehaviourManager).selectBehaviour(mockEmotion);
        verify(mockEmotionalAgent).addBehaviour(mockBehaviour);
        verify(mockEmotionalAgent, never()).removeBehaviour(mockBehaviour);
        assertThat(mockEmotionalAgent.getEmotionalBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldRemoveEmotionalBehaviour() {
        mockEmotionalAgent.evaluateStimulus(any());
        mockEmotionalAgent.evaluateStimulus(any());
        verify(mockEmotionalAgent).removeBehaviour(mockBehaviour);
    }

    @Test
    public void shouldUpdateEmotion() {
        mockEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockEmotionalConfigurator).updateEmotionalState(mockStimulus);
        assertThat(mockEmotionalAgent.getCurrentEmotion(), is(mockEmotion));
    }

}