/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BehaviourManagerTest {

    private BehaviourManager mockBehaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalAgent mockEmotionalAgent;

    @Before
    public void setUp() {
        mockBehaviourManager = mock(BehaviourManager.class);
        mockBehaviour = mock(Behaviour.class);
        mockEmotionalAgent = mock(EmotionalAgent.class);

        doCallRealMethod().when(mockBehaviourManager).getBehaviourTypeAssociated(any());
        doCallRealMethod().when(mockBehaviourManager).updateEmotionalBehaviour(any(), any());
        doCallRealMethod().when(mockBehaviourManager).getBehaviour();
        doReturn(mockBehaviour).when(mockBehaviourManager).evaluateEmotion(any());
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(mockBehaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(mockBehaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(mockBehaviourManager.getBehaviourTypeAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

    @Test
    public void shouldInvokeEvaluateEmotionAndUpdateBehaviour() {
        mockBehaviourManager.updateEmotionalBehaviour(null, mockEmotionalAgent);
        verify(mockBehaviourManager).evaluateEmotion(any());
        verify(mockEmotionalAgent, never()).removeBehaviour(any());
        verify(mockEmotionalAgent).addBehaviour(mockBehaviour);
        assertThat(mockBehaviourManager.getBehaviour(), is(mockBehaviour));

        mockBehaviourManager.updateEmotionalBehaviour(null, mockEmotionalAgent);
        verify(mockEmotionalAgent).removeBehaviour(mockBehaviour);
    }

}