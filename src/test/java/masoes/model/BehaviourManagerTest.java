/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

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
    private Behaviour behaviourMock;
    private EmotionalAgent emotionalAgentMock;
    private Emotion emotionMock;

    @Before
    public void setUp() {
        behaviourManager = new BehaviourManager();
        emotionalAgentMock = mock(EmotionalAgent.class);
        behaviourMock = mock(Behaviour.class);
        emotionMock = mock(Emotion.class);
        doReturn(emotionMock).when(emotionalAgentMock).getCurrentEmotion();
        doReturn(EmotionType.NEGATIVE_LOW).when(emotionMock).getEmotionType();
        doReturn(behaviourMock).when(emotionalAgentMock).getCognitiveBehaviour();
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

    @Test
    public void shouldUpdateCognitiveBehaviour() {
        doReturn(EmotionType.NEGATIVE_LOW).when(emotionMock).getEmotionType();
        doReturn(behaviourMock).when(emotionalAgentMock).getCognitiveBehaviour();
        behaviourManager.updateBehaviour(emotionalAgentMock);
        verify(emotionalAgentMock).getCognitiveBehaviour();
        assertThat(behaviourManager.getBehaviour(), is(behaviourMock));
    }

    @Test
    public void shouldUpdateImitativeBehaviour() {
        doReturn(EmotionType.POSITIVE).when(emotionMock).getEmotionType();
        doReturn(behaviourMock).when(emotionalAgentMock).getImitativeBehaviour();
        behaviourManager.updateBehaviour(emotionalAgentMock);
        verify(emotionalAgentMock).getImitativeBehaviour();
        assertThat(behaviourManager.getBehaviour(), is(behaviourMock));
    }

    @Test
    public void shouldUpdateReactiveBehaviour() {
        doReturn(EmotionType.NEGATIVE_HIGH).when(emotionMock).getEmotionType();
        doReturn(behaviourMock).when(emotionalAgentMock).getReactiveBehaviour();
        behaviourManager.updateBehaviour(emotionalAgentMock);
        verify(emotionalAgentMock).getReactiveBehaviour();
        assertThat(behaviourManager.getBehaviour(), is(behaviourMock));
    }

    @Test
    public void shouldChangeEmotionalBehaviour() throws Exception {
        Behaviour behaviourMock = mock(Behaviour.class);
        setFieldValue(behaviourManager, "behaviour", behaviourMock);
        behaviourManager.updateBehaviour(emotionalAgentMock);
        verify(emotionalAgentMock).removeBehaviour(behaviourMock);
        verify(emotionalAgentMock).addBehaviour(this.behaviourMock);
    }

    @Test
    public void shouldNotInvokeRemoveBehaviour() throws Exception {
        setFieldValue(behaviourManager, "behaviour", null);
        behaviourManager.updateBehaviour(emotionalAgentMock);
        verify(emotionalAgentMock, never()).removeBehaviour(any());
    }

    @Test
    public void shouldNotInvokeAddBehaviour() {
        doReturn(null).when(emotionalAgentMock).getCognitiveBehaviour();
        behaviourManager.updateBehaviour(emotionalAgentMock);
        verify(emotionalAgentMock, never()).addBehaviour(any());
    }

}