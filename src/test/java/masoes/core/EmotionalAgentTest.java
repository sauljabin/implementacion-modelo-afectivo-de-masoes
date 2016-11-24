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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmotionalAgentTest {

    private EmotionalAgent spyEmotionalAgent;
    private BehaviourManager mockBehaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalConfigurator mockEmotionalConfigurator;
    private Stimulus mockStimulus;
    private Emotion mockEmotion;
    private EmotionalModel emotionalModel;

    @Before
    public void setUp() {
        mockBehaviourManager = mock(BehaviourManager.class);
        mockBehaviour = mock(Behaviour.class);
        mockEmotionalConfigurator = mock(EmotionalConfigurator.class);
        mockEmotion = mock(Emotion.class);
        mockStimulus = mock(Stimulus.class);

        emotionalModel = new EmotionalModel(mockEmotionalConfigurator, mockBehaviourManager);
        spyEmotionalAgent = spy(createDummyEmotionalAgent(emotionalModel));

        when(mockBehaviourManager.selectBehaviour(any())).thenReturn(null);
        when(mockEmotionalConfigurator.getEmotion()).thenReturn(mockEmotion);
    }

    @Test
    public void shouldAddBasicBehaviors() {
        when(mockBehaviourManager.selectBehaviour(any())).thenReturn(mockBehaviour);
        spyEmotionalAgent.setup();
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(any(ReplayAgentInformationBehaviour.class));
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(any(StimulusReceiverBehaviour.class));
        assertThat(spyEmotionalAgent.getEmotionalBehaviour(), is(mockBehaviour));
        assertThat(spyEmotionalAgent.getCurrentEmotion(), is(mockEmotion));
    }

    @Test
    public void shouldChangeEmotionalBehaviour() {
        spyEmotionalAgent.setup();
        when(mockBehaviourManager.selectBehaviour(any())).thenReturn(mockBehaviour);
        spyEmotionalAgent.evaluateStimulus(any());
        verify(mockBehaviourManager, atLeastOnce()).selectBehaviour(mockEmotion);
        verify(spyEmotionalAgent).addBehaviour(mockBehaviour);
        verify(spyEmotionalAgent, never()).removeBehaviour(mockBehaviour);
        assertThat(spyEmotionalAgent.getEmotionalBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldRemoveEmotionalBehaviour() {
        spyEmotionalAgent.setup();
        when(mockBehaviourManager.selectBehaviour(any())).thenReturn(mockBehaviour);
        spyEmotionalAgent.evaluateStimulus(any());
        spyEmotionalAgent.evaluateStimulus(any());
        verify(spyEmotionalAgent).removeBehaviour(mockBehaviour);
    }

    @Test
    public void shouldUpdateEmotion() {
        when(mockBehaviourManager.selectBehaviour(any())).thenReturn(mockBehaviour);
        spyEmotionalAgent.setup();
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockEmotionalConfigurator).updateEmotionalState(mockStimulus);
        assertThat(spyEmotionalAgent.getCurrentEmotion(), is(mockEmotion));
    }

    private EmotionalAgent createDummyEmotionalAgent(EmotionalModel emotionalModel) {
        return new EmotionalAgent() {
            @Override
            protected EmotionalModel createEmotionalModel() {
                return emotionalModel;
            }

            @Override
            protected void setUp() {

            }
        };
    }

}