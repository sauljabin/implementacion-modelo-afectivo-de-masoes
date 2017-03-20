/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import jade.core.AID;
import knowledge.Knowledge;
import masoes.EmotionalAgent;
import masoes.EmotionalAgentLogger;
import masoes.behavioural.emotion.CompassionEmotion;
import masoes.behavioural.emotion.DepressionEmotion;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.JoyEmotion;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class EmotionalConfiguratorTest extends PowerMockitoTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private EmotionalConfigurator emotionalConfigurator;
    private EmotionalAgent agentMock;
    private EmotionalAgentLogger loggerMock;

    @Before
    public void setUp() {
        createAgent(AGENT_NAME);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
    }

    private void createAgent(String agentName) {
        agentMock = mock(EmotionalAgent.class);
        loggerMock = mock(EmotionalAgentLogger.class);
        doReturn(new Knowledge(Paths.get(AGENT_KNOWLEDGE_PATH))).when(agentMock).getKnowledge();
        doReturn(agentName).when(agentMock).getLocalName();
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
        ObjectStimulus stimulus = new ObjectStimulus(new AID(AGENT_NAME, AID.ISGUID), "no-object");
        emotionalConfigurator.updateEmotion(stimulus);
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldReturnSameEmotionAndStateWhenRuleEventNotFound() {
        EmotionalState emotionalState = emotionalConfigurator.getEmotionalState();
        EventStimulus stimulus = new EventStimulus(new AID(AGENT_NAME, AID.ISGUID), "no-event");
        emotionalConfigurator.updateEmotion(stimulus);
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldIncreaseActivationAndSatisfaction() {
        double initialState = .45;
        EmotionalState emotionalState = new EmotionalState(initialState, initialState);
        emotionalConfigurator.setEmotionalState(emotionalState);

        ActionStimulus stimulus = new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), "eat");

        emotionalConfigurator.updateEmotion(stimulus);

        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(HappinessEmotion.class)));
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(greaterThan(initialState)));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(greaterThan(initialState)));
    }

    @Test
    public void shouldDecreaseActivationAndSatisfaction() {
        double initialState = .55;
        EmotionalState emotionalState = new EmotionalState(initialState, initialState);
        emotionalConfigurator.setEmotionalState(emotionalState);

        ObjectStimulus stimulus = new ObjectStimulus(new AID(AGENT_NAME, AID.ISGUID), "wake");

        emotionalConfigurator.updateEmotion(stimulus);

        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(JoyEmotion.class)));
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(lessThan(initialState)));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(lessThan(initialState)));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithActionAndUpperAgent() {
        String agentName = "AGENT_NAME";
        createAgent(agentName);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);

        double initialState = -.55;
        EmotionalState emotionalState = new EmotionalState(initialState, initialState);
        emotionalConfigurator.setEmotionalState(emotionalState);

        ActionStimulus stimulus = new ActionStimulus(new AID(agentName, AID.ISGUID), "wake");

        emotionalConfigurator.updateEmotion(stimulus);

        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(DepressionEmotion.class)));
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(lessThan(initialState)));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(lessThan(initialState)));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperAction() {
        behaviouralKnowledgeBase.addTheory("valence(AGENT, 'Eat', negative, positive) :- self(AGENT).");

        EmotionalState emotionalState = new EmotionalState(-.45, .45);
        emotionalConfigurator.setEmotionalState(emotionalState);

        EventStimulus stimulus = new EventStimulus(new AID(AGENT_NAME, AID.ISGUID), "Eat");

        emotionalConfigurator.updateEmotion(stimulus);

        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(CompassionEmotion.class)));
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(lessThan(-.45)));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(greaterThan(.45)));
    }

    @Test
    public void shouldReturnRandomValueWhenResultHasMoreThanOneSolution() {
        String actionName = "actionName";
        behaviouralKnowledgeBase.addTheory("valence(AGENT, actionName, positive, positive) :- self(AGENT).");
        behaviouralKnowledgeBase.addTheory("valence(AGENT, actionName, negative, negative) :- self(AGENT).");

        List<Emotion> emotions = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            emotionalConfigurator.setEmotionalState(new EmotionalState(.55, .55));
            emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), actionName));
            emotions.add(emotionalConfigurator.getEmotion());
        }

        assertThat(emotions, hasItem(instanceOf(JoyEmotion.class)));
        assertThat(emotions, hasItem(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldNotInvokeLoggerUpdatingEmotion() throws Exception {
        createAgent(AGENT_NAME);
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        setFieldValue(emotionalConfigurator, "emotionalState", new JoyEmotion().getRandomEmotionalState());

        emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), "sleep"));
        verify(loggerMock, never()).updatingEmotion(any(Emotion.class), eq(emotionalConfigurator.getEmotion()));
    }

}