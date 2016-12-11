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
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class BehaviourManagerTest {

    private BehaviourManager behaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalAgent mockEmotionalAgent;
    private Emotion mockEmotion;
    private BehaviourFactory mockBehaviourFactory;

    @Before
    public void setUp() {
        behaviourManager = new BehaviourManager();
        mockEmotionalAgent = mock(EmotionalAgent.class);
        mockBehaviour = mock(Behaviour.class);
        mockEmotion = mock(Emotion.class);
        mockBehaviourFactory = mock(BehaviourFactory.class);
        when(mockEmotionalAgent.getCurrentEmotion()).thenReturn(mockEmotion);
        when(mockEmotionalAgent.getBehaviourFactory()).thenReturn(mockBehaviourFactory);
        when(mockEmotion.getEmotionType()).thenReturn(EmotionType.NEGATIVE_LOW);
        when(mockBehaviourFactory.createCognitiveBehaviour(mockEmotionalAgent)).thenReturn(mockBehaviour);
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

    @Test
    public void shouldUpdateCognitiveBehaviour() {
        when(mockEmotion.getEmotionType()).thenReturn(EmotionType.NEGATIVE_LOW);
        when(mockBehaviourFactory.createCognitiveBehaviour(mockEmotionalAgent)).thenReturn(mockBehaviour);
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockBehaviourFactory).createCognitiveBehaviour(mockEmotionalAgent);
        assertThat(behaviourManager.getBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldUpdateImitativeBehaviour() {
        when(mockEmotion.getEmotionType()).thenReturn(EmotionType.POSITIVE);
        when(mockBehaviourFactory.createImitativeBehaviour(mockEmotionalAgent)).thenReturn(mockBehaviour);
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockBehaviourFactory).createImitativeBehaviour(mockEmotionalAgent);
        assertThat(behaviourManager.getBehaviour(), is(mockBehaviour));
    }

    @Test
    public void shouldUpdateReactiveBehaviour() {
        when(mockEmotion.getEmotionType()).thenReturn(EmotionType.NEGATIVE_HIGH);
        when(mockBehaviourFactory.createReactiveBehaviour(mockEmotionalAgent)).thenReturn(mockBehaviour);
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockBehaviourFactory).createReactiveBehaviour(mockEmotionalAgent);
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
    public void shouldReturnNullIfBehaviourFactoryIsNull() {
        when(mockEmotionalAgent.getBehaviourFactory()).thenReturn(null);
        assertThat(behaviourManager.calculateBehaviour(mockEmotionalAgent), is(nullValue()));
    }

    @Test
    public void shouldNotInvokeRemoveBehaviour() throws Exception {
        setFieldValue(behaviourManager, "behaviour", null);
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent, never()).removeBehaviour(any());
    }

    @Test
    public void shouldNotInvokeAddBehaviour() {
        when(mockEmotionalAgent.getBehaviourFactory()).thenReturn(null);
        behaviourManager.updateBehaviour(mockEmotionalAgent);
        verify(mockEmotionalAgent, never()).addBehaviour(any());
    }

}