/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import jade.core.behaviours.Behaviour;
import masoes.CognitiveBehaviour;
import masoes.EmotionalAgent;
import masoes.EmotionalBehaviour;
import masoes.ImitativeBehaviour;
import masoes.ReactiveBehaviour;
import masoes.behavioural.emotion.AdmirationEmotion;
import masoes.behavioural.emotion.AngerEmotion;
import masoes.behavioural.emotion.CompassionEmotion;
import masoes.behavioural.emotion.DepressionEmotion;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.JoyEmotion;
import masoes.behavioural.emotion.RejectionEmotion;
import masoes.behavioural.emotion.SadnessEmotion;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class BehaviourManagerTest extends PowerMockitoTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private BehaviourManager behaviourManager;
    private EmotionalAgent agentMock;
    private ImitativeBehaviour imitativeBehaviourMock;
    private ReactiveBehaviour reactiveBehaviourMock;
    private CognitiveBehaviour cognitiveBehaviourMock;
    private EmotionalBehaviour currentBehaviourMock;

    @Before
    public void setUp() throws Exception {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME, AGENT_KNOWLEDGE_PATH);
        behaviourManager = new BehaviourManager(behaviouralKnowledgeBase);
        agentMock = mock(EmotionalAgent.class);
        doNothing().when(agentMock).addBehaviour(any());
        doNothing().when(agentMock).removeBehaviour(any());

        imitativeBehaviourMock = mock(ImitativeBehaviour.class);
        doReturn(imitativeBehaviourMock).when(agentMock).getImitativeBehaviour();

        reactiveBehaviourMock = mock(ReactiveBehaviour.class);
        doReturn(reactiveBehaviourMock).when(agentMock).getReactiveBehaviour();

        cognitiveBehaviourMock = mock(CognitiveBehaviour.class);
        doReturn(cognitiveBehaviourMock).when(agentMock).getCognitiveBehaviour();

        currentBehaviourMock = mock(EmotionalBehaviour.class);
        setFieldValue(behaviourManager, "behaviour", currentBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithHappinessEmotion() {
        testUpdateBehaviour(new HappinessEmotion(), imitativeBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithCompassionEmotion() {
        testUpdateBehaviour(new CompassionEmotion(), imitativeBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithJoyEmotion() {
        testUpdateBehaviour(new JoyEmotion(), imitativeBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithAdmirationEmotion() {
        testUpdateBehaviour(new AdmirationEmotion(), imitativeBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithRejectionEmotion() {
        testUpdateBehaviour(new RejectionEmotion(), cognitiveBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithSadnessEmotion() {
        testUpdateBehaviour(new SadnessEmotion(), cognitiveBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithDepressionEmotion() {
        testUpdateBehaviour(new DepressionEmotion(), reactiveBehaviourMock);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithAngerEmotion() {
        testUpdateBehaviour(new AngerEmotion(), reactiveBehaviourMock);
    }

    @Test
    public void shouldUpdateBehaviourWithUpperEmotionName() {
        behaviouralKnowledgeBase.addTheory("emotionType('TEST', positive).");
        Emotion emotionMock = mock(Emotion.class);
        doReturn(BehaviourType.REACTIVE).when(currentBehaviourMock).getType();
        doReturn("TEST").when(emotionMock).getName();
        behaviourManager.updateBehaviour(agentMock, emotionMock);
        assertThat(behaviourManager.getBehaviour(), is(imitativeBehaviourMock));
    }

    @Test
    public void shouldReturnSameBehaviourWhenTypeNotChange() throws Exception {
        HappinessEmotion emotion = new HappinessEmotion();
        doReturn(BehaviourType.IMITATIVE).when(currentBehaviourMock).getType();
        behaviourManager.updateBehaviour(agentMock, emotion);
        assertThat(behaviourManager.getBehaviour(), is(currentBehaviourMock));
        verify(agentMock, never()).addBehaviour(imitativeBehaviourMock);
        verify(agentMock, never()).removeBehaviour(currentBehaviourMock);
    }

    private void testUpdateBehaviour(Emotion emotion, Behaviour expectedBehaviour) {
        behaviourManager.updateBehaviour(agentMock, emotion);
        assertThat(behaviourManager.getBehaviour(), is(expectedBehaviour));
        verify(agentMock).addBehaviour(expectedBehaviour);
        verify(agentMock).removeBehaviour(currentBehaviourMock);
    }

}