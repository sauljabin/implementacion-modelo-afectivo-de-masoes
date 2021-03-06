/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import jade.core.AID;
import masoes.component.behavioural.emotion.CompassionEmotion;
import masoes.component.behavioural.emotion.DepressionEmotion;
import masoes.component.behavioural.emotion.HappinessEmotion;
import masoes.component.behavioural.emotion.JoyEmotion;
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
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class EmotionalConfiguratorTest extends PowerMockitoTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private EmotionalConfigurator emotionalConfigurator;

    @Before
    public void setUp() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME);
        behaviouralKnowledgeBase.addTheory(Paths.get(AGENT_KNOWLEDGE_PATH));
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
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
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(initialState + .5));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(initialState + .5));
    }

    @Test
    public void shouldDecreaseActivationAndSatisfaction() {
        double initialState = .55;
        EmotionalState emotionalState = new EmotionalState(initialState, initialState);
        emotionalConfigurator.setEmotionalState(emotionalState);

        ObjectStimulus stimulus = new ObjectStimulus(new AID(AGENT_NAME, AID.ISGUID), "wake");

        emotionalConfigurator.updateEmotion(stimulus);

        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(JoyEmotion.class)));
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(initialState - .3));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(initialState - .5));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithActionAndUpperAgent() {
        String agentName = "AGENT_NAME";
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentName);
        behaviouralKnowledgeBase.addTheory(Paths.get(AGENT_KNOWLEDGE_PATH));
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);

        double initialState = -.1;
        EmotionalState emotionalState = new EmotionalState(initialState, initialState);
        emotionalConfigurator.setEmotionalState(emotionalState);

        ActionStimulus stimulus = new ActionStimulus(new AID(agentName, AID.ISGUID), "wake");

        emotionalConfigurator.updateEmotion(stimulus);

        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(DepressionEmotion.class)));
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(initialState - .3));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(initialState - .5));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperAction() {
        behaviouralKnowledgeBase.addTheory("stimulus(AGENT, 'Eat', -0.1, -0.1) :- self(AGENT).");

        EmotionalState emotionalState = new EmotionalState(-.45, .45);
        emotionalConfigurator.setEmotionalState(emotionalState);

        EventStimulus stimulus = new EventStimulus(new AID(AGENT_NAME, AID.ISGUID), "Eat");

        emotionalConfigurator.updateEmotion(stimulus);

        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(CompassionEmotion.class)));
        assertThat(emotionalConfigurator.getEmotionalState().getActivation(), is(-.55));
        assertThat(emotionalConfigurator.getEmotionalState().getSatisfaction(), is(.35));
    }

    @Test
    public void shouldReturnRandomValueWhenResultHasMoreThanOneSolution() {
        String actionName = "actionName";
        behaviouralKnowledgeBase.addTheory("stimulus(AGENT, actionName, 0.1, 0.1) :- self(AGENT).");
        behaviouralKnowledgeBase.addTheory("stimulus(AGENT, actionName, -0.1, -0.1) :- self(AGENT).");

        List<Emotion> emotions = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            emotionalConfigurator.setEmotionalState(new EmotionalState(.55, .55));
            emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), actionName));
            emotions.add(emotionalConfigurator.getEmotion());
        }

        assertThat(emotions, hasItem(instanceOf(JoyEmotion.class)));
        assertThat(emotions, hasItem(instanceOf(HappinessEmotion.class)));
    }

}