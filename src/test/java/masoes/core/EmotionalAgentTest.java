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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmotionalAgentTest {

    private final EmotionalState mockEmotionalState;
    private EmotionalAgent spyEmotionalAgent;
    private BehaviourManager mockBehaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalConfigurator mockEmotionalConfigurator;
    private Emotion mockEmotion;
    private EmotionalModel emotionalModel;
    private Stimulus mockStimulus;

    public EmotionalAgentTest() {
        mockEmotionalState = mock(EmotionalState.class);
    }

    @Before
    public void setUp() {
        mockBehaviourManager = mock(BehaviourManager.class);
        mockEmotionalConfigurator = mock(EmotionalConfigurator.class);

        emotionalModel = new EmotionalModel(mockEmotionalConfigurator, mockBehaviourManager);
        spyEmotionalAgent = spy(createDummyEmotionalAgent(emotionalModel));

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
    }

    @Test
    public void shouldChangeEmotionalBehaviour() {
        spyEmotionalAgent.evaluateStimulus(any());
        verify(mockBehaviourManager, times(2)).updateBehaviour(mockEmotion);
        verify(spyEmotionalAgent, times(2)).addBehaviour(mockBehaviour);
        verify(spyEmotionalAgent).removeBehaviour(mockBehaviour);
        assertThat(spyEmotionalAgent.getEmotionalBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldUpdateEmotion() {
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockEmotionalConfigurator).updateEmotionalState(mockStimulus);
        assertThat(spyEmotionalAgent.getCurrentEmotion(), is(mockEmotion));
        assertThat(spyEmotionalAgent.getEmotionalState(), is(mockEmotionalState));
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