/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import jade.core.AID;
import jade.util.leap.ArrayList;
import knowledge.Knowledge;
import masoes.EmotionalAgent;
import masoes.EmotionalAgentLogger;
import masoes.behavioural.emotion.AdmirationEmotion;
import masoes.behavioural.emotion.AngerEmotion;
import masoes.behavioural.emotion.CompassionEmotion;
import masoes.behavioural.emotion.DepressionEmotion;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.JoyEmotion;
import masoes.behavioural.emotion.RejectionEmotion;
import masoes.behavioural.emotion.SadnessEmotion;
import ontology.masoes.ActionStimulus;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class EmotionalConfiguratorTest extends PowerMockitoTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private EmotionalConfigurator emotionalConfigurator;
    private EmotionalAgent agentMock;
    private EmotionalAgentLogger loggerMock;

    @Before
    public void setUp() {
        createAgent(AGENT_NAME);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(agentMock, behaviouralKnowledgeBase);
    }

    private void createAgent(String agentName) {
        agentMock = mock(EmotionalAgent.class);
        loggerMock = mock(EmotionalAgentLogger.class);
        doReturn(new Knowledge(Paths.get(AGENT_KNOWLEDGE_PATH))).when(agentMock).getKnowledge();
        doReturn(agentName).when(agentMock).getLocalName();
        doReturn(loggerMock).when(agentMock).getLogger();
    }

    @Test
    public void shouldReturnSameEmotionAndStateWhenRuleActionNotFound() {
        EmotionalState emotionalState = emotionalConfigurator.getEmotionalState();
        emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), "no-action"));
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldReturnSameEmotionAndStateWhenRuleObjectNotFound() {
        EmotionalState emotionalState = emotionalConfigurator.getEmotionalState();
        ObjectStimulus stimulus = new ObjectStimulus(new AID(AGENT_NAME, AID.ISGUID), "no-object", new ArrayList());
        emotionalConfigurator.updateEmotion(stimulus);
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldReturnSameEmotionAndStateWhenEmotionNotFound() {
        String actionName = "actionName";
        EmotionalState emotionalState = emotionalConfigurator.getEmotionalState();
        behaviouralKnowledgeBase.addTheory("satisfactionByAction(AGENT, actionName, newLevel) :- self(AGENT).");
        behaviouralKnowledgeBase.addTheory("emotionByAction(AGENT, ACTION, newEmotion) :- satisfactionByAction(AGENT, ACTION, newLevel), self(AGENT).");
        emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), actionName));
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldReturnRandomValueWhenResultHasMoreThanOneSolution() {
        String actionName = "actionName";
        behaviouralKnowledgeBase.addTheory("satisfactionByAction(AGENT, actionName, positive_high) :- self(AGENT).");
        behaviouralKnowledgeBase.addTheory("satisfactionByAction(AGENT, actionName, positive_low) :- self(AGENT).");

        List<Emotion> emotions = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), actionName));
            emotions.add(emotionalConfigurator.getEmotion());
        }

        assertThat(emotions, hasItem(instanceOf(JoyEmotion.class)));
        assertThat(emotions, hasItem(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithAction() {
        testUpdateEmotionWithAction(AGENT_NAME, HappinessEmotion.class, "eat");
        testUpdateEmotionWithAction(AGENT_NAME, JoyEmotion.class, "sleep");
        testUpdateEmotionWithAction(AGENT_NAME, SadnessEmotion.class, "wake");
        testUpdateEmotionWithAction(AGENT_NAME, DepressionEmotion.class, "pay");
        testUpdateEmotionWithAction("other", CompassionEmotion.class, "greeting");
        testUpdateEmotionWithAction("other", AdmirationEmotion.class, "smile");
        testUpdateEmotionWithAction("other", RejectionEmotion.class, "run");
        testUpdateEmotionWithAction("other", AngerEmotion.class, "bye");
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithObject() {
        testUpdateEmotionWithObject(AGENT_NAME, HappinessEmotion.class, new ObjectProperty("color", "blue"));
        testUpdateEmotionWithObject(AGENT_NAME, JoyEmotion.class, new ObjectProperty("color", "red"));
        testUpdateEmotionWithObject(AGENT_NAME, SadnessEmotion.class, new ObjectProperty("color", "white"));
        testUpdateEmotionWithObject(AGENT_NAME, DepressionEmotion.class, new ObjectProperty("color", "black"));
        testUpdateEmotionWithObject("other", CompassionEmotion.class, new ObjectProperty("color", "blue"));
        testUpdateEmotionWithObject("other", AdmirationEmotion.class, new ObjectProperty("color", "red"));
        testUpdateEmotionWithObject("other", RejectionEmotion.class, new ObjectProperty("color", "white"));
        testUpdateEmotionWithObject("other", AngerEmotion.class, new ObjectProperty("color", "black"));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithActionAndUpperAgent() {
        String agentName = "AGENT_NAME";
        createAgent(agentName);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(agentMock, behaviouralKnowledgeBase);
        testUpdateEmotionWithAction(agentName, HappinessEmotion.class, "eat");
        testUpdateEmotionWithAction(agentName, JoyEmotion.class, "sleep");
        testUpdateEmotionWithAction(agentName, SadnessEmotion.class, "wake");
        testUpdateEmotionWithAction(agentName, DepressionEmotion.class, "pay");
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithObjectAndUpperAgent() {
        String agentName = "AGENT_NAME";
        createAgent(agentName);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(agentMock, behaviouralKnowledgeBase);
        testUpdateEmotionWithObject(agentName, HappinessEmotion.class, new ObjectProperty("color", "blue"));
        testUpdateEmotionWithObject(agentName, JoyEmotion.class, new ObjectProperty("color", "red"));
        testUpdateEmotionWithObject(agentName, SadnessEmotion.class, new ObjectProperty("color", "white"));
        testUpdateEmotionWithObject(agentName, DepressionEmotion.class, new ObjectProperty("color", "black"));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperAction() {
        behaviouralKnowledgeBase.addTheory("satisfactionByAction(AGENT, 'Eat', positive_low) :- self(AGENT).");
        testUpdateEmotionWithAction(AGENT_NAME, JoyEmotion.class, "Eat");
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperObject() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(agentMock, behaviouralKnowledgeBase);
        behaviouralKnowledgeBase.addTheory("satisfactionByObject(AGENT, PROPERTIES, positive_high) :- member(color='Gray', PROPERTIES).");
        testUpdateEmotionWithObject(AGENT_NAME, HappinessEmotion.class, new ObjectProperty("color", "Gray"));
    }

    @Test
    public void shouldInvokeLoggerUpdatingEmotion() throws Exception {
        createAgent(AGENT_NAME);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(agentMock, behaviouralKnowledgeBase);
        setFieldValue(emotionalConfigurator, "emotionalState", new CompassionEmotion().getRandomEmotionalState());

        emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), "sleep"));
        verify(loggerMock).updatingEmotion(any(Emotion.class), eq(emotionalConfigurator.getEmotion()));
    }

    @Test
    public void shouldNotInvokeLoggerUpdatingEmotion() throws Exception {
        createAgent(AGENT_NAME);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(agentMock, behaviouralKnowledgeBase);
        setFieldValue(emotionalConfigurator, "emotionalState", new JoyEmotion().getRandomEmotionalState());

        emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), "sleep"));
        verify(loggerMock, never()).updatingEmotion(any(Emotion.class), eq(emotionalConfigurator.getEmotion()));
    }

    private void testUpdateEmotionWithAction(String agentName, Class<? extends Emotion> expectedEmotion, String actionName) {
        emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(agentName, AID.ISGUID), actionName));
        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(expectedEmotion)));
        assertTrue(emotionalConfigurator.getEmotion().getGeometry().intersects(emotionalConfigurator.getEmotionalState().toPoint()));
    }

    private void testUpdateEmotionWithObject(String agentName, Class<? extends Emotion> expectedEmotion, ObjectProperty... objectProperties) {
        ObjectStimulus objectStimulus = new ObjectStimulus();
        objectStimulus.setCreator(new AID(agentName, AID.ISGUID));
        ArrayList properties = new ArrayList();
        properties.fromList(Arrays.asList(objectProperties));
        objectStimulus.setObjectProperties(properties);
        emotionalConfigurator.updateEmotion(objectStimulus);
        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(expectedEmotion)));
        assertTrue(emotionalConfigurator.getEmotion().getGeometry().intersects(emotionalConfigurator.getEmotionalState().toPoint()));
    }

}