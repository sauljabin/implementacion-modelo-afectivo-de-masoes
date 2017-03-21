/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import knowledge.Knowledge;
import masoes.agent.EmotionalAgent;
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

import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class BehaviourManagerTest extends PowerMockitoTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private BehaviourManager behaviourManager;
    private EmotionalAgent agentMock;
    private EmotionalConfigurator emotionalConfiguratorMock;

    @Before
    public void setUp() {
        agentMock = mock(EmotionalAgent.class);
        doReturn(new Knowledge(Paths.get(AGENT_KNOWLEDGE_PATH))).when(agentMock).getKnowledge();
        doReturn(AGENT_NAME).when(agentMock).getLocalName();

        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfiguratorMock = mock(EmotionalConfigurator.class);
        doReturn(new HappinessEmotion()).when(emotionalConfiguratorMock).getEmotion();

        behaviourManager = new BehaviourManager(emotionalConfiguratorMock, behaviouralKnowledgeBase);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithHappinessEmotion() {
        testUpdateBehaviour(new HappinessEmotion(), BehaviourType.IMITATIVE);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithCompassionEmotion() {
        testUpdateBehaviour(new CompassionEmotion(), BehaviourType.IMITATIVE);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithJoyEmotion() {
        testUpdateBehaviour(new JoyEmotion(), BehaviourType.IMITATIVE);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithAdmirationEmotion() {
        testUpdateBehaviour(new AdmirationEmotion(), BehaviourType.IMITATIVE);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithRejectionEmotion() {
        testUpdateBehaviour(new RejectionEmotion(), BehaviourType.COGNITIVE);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithSadnessEmotion() {
        testUpdateBehaviour(new SadnessEmotion(), BehaviourType.COGNITIVE);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithDepressionEmotion() {
        testUpdateBehaviour(new DepressionEmotion(), BehaviourType.REACTIVE);
    }

    @Test
    public void shouldUpdateCorrectlyTheBehaviourWithAngerEmotion() {
        testUpdateBehaviour(new AngerEmotion(), BehaviourType.REACTIVE);
    }

    private void testUpdateBehaviour(Emotion emotion, BehaviourType behaviourType) {
        doReturn(emotion).when(emotionalConfiguratorMock).getEmotion();
        behaviourManager.updateBehaviour();
        assertThat(behaviourManager.getBehaviourType(), is(behaviourType));
    }

    @Test
    public void shouldUpdateBehaviourWithUpperEmotionName() throws Exception {
        setFieldValue(behaviourManager, "behaviourType", BehaviourType.REACTIVE);
        behaviouralKnowledgeBase.addTheory("emotionType('TEST', positive).");
        Emotion emotionMock = mock(Emotion.class);
        doReturn("TEST").when(emotionMock).getName();
        doReturn(emotionMock).when(emotionalConfiguratorMock).getEmotion();
        behaviourManager.updateBehaviour();
        assertThat(behaviourManager.getBehaviourType(), is(BehaviourType.IMITATIVE));
    }

}