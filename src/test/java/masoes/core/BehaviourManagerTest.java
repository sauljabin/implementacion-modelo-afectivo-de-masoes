/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class BehaviourManagerTest {

    private BehaviourManager behaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalAgent mockEmotionalAgent;
    private Emotion mockEmotion;

    @Before
    public void setUp() {
        behaviourManager = new BehaviourManager();
        mockEmotionalAgent = mock(EmotionalAgent.class);
        mockBehaviour = mock(Behaviour.class);
        mockEmotion = mock(Emotion.class);
        doReturn(mockEmotion).when(mockEmotionalAgent).getCurrentEmotion();
        doReturn(EmotionType.NEGATIVE_LOW).when(mockEmotion).getEmotionType();
        doReturn(mockBehaviour).when(mockEmotionalAgent).getCognitiveBehaviour();
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

    @Test
    public void shouldUpdateCognitiveBehaviour() {
        doReturn(EmotionType.NEGATIVE_LOW).when(mockEmotion).getEmotionType();
        doReturn(mockBehaviour).when(mockEmotionalAgent).getCognitiveBehaviour();
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent).getCognitiveBehaviour();
        assertThat(behaviourManager.getBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldUpdateImitativeBehaviour() {
        doReturn(EmotionType.POSITIVE).when(mockEmotion).getEmotionType();
        doReturn(mockBehaviour).when(mockEmotionalAgent).getImitativeBehaviour();
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent).getImitativeBehaviour();
        assertThat(behaviourManager.getBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldUpdateReactiveBehaviour() {
        doReturn(EmotionType.NEGATIVE_HIGH).when(mockEmotion).getEmotionType();
        doReturn(mockBehaviour).when(mockEmotionalAgent).getReactiveBehaviour();
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent).getReactiveBehaviour();
        assertThat(behaviourManager.getBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldChangeEmotionalBehaviour() throws Exception {
        Behaviour mockBehaviourToRemove = mock(Behaviour.class);
        setFieldValue(behaviourManager, "behaviour", mockBehaviourToRemove);
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent).removeBehaviour(mockBehaviourToRemove);
        verify(mockEmotionalAgent).addBehaviour(mockBehaviour);
    }

    @Test
    public void shouldNotInvokeRemoveBehaviour() throws Exception {
        setFieldValue(behaviourManager, "behaviour", null);
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent, never()).removeBehaviour(any());
    }

    @Test
    public void shouldNotInvokeAddBehaviour() {
        doReturn(null).when(mockEmotionalAgent).getCognitiveBehaviour();
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent, never()).addBehaviour(any());
    }

}