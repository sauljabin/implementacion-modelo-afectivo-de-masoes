/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import jade.core.AID;
import jade.util.leap.ArrayList;
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

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;


public class EmotionalConfiguratorTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private EmotionalConfigurator emotionalConfigurator;

    @Before
    public void setUp() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME, AGENT_KNOWLEDGE_PATH);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
    }

    @Test
    public void shouldReturnSameEmotionAndStateWhenRuleActionNotFound() throws Exception {
        EmotionalState emotionalState = emotionalConfigurator.getEmotionalState();
        emotionalConfigurator.updateEmotion(new ActionStimulus(new AID(AGENT_NAME, AID.ISGUID), "no-action"));
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldReturnSameEmotionAndStateWhenRuleObjectNotFound() throws Exception {
        EmotionalState emotionalState = emotionalConfigurator.getEmotionalState();
        ObjectStimulus stimulus = new ObjectStimulus(new AID(AGENT_NAME, AID.ISGUID), "no-object", new ArrayList());
        emotionalConfigurator.updateEmotion(stimulus);
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithAction() {
        testUpdateEmotionWithAction("other", CompassionEmotion.class, "greeting");
        testUpdateEmotionWithAction("other", AdmirationEmotion.class, "smile");
        testUpdateEmotionWithAction("other", RejectionEmotion.class, "run");
        testUpdateEmotionWithAction("other", AngerEmotion.class, "bye");
        testUpdateEmotionWithAction(AGENT_NAME, HappinessEmotion.class, "eat");
        testUpdateEmotionWithAction(AGENT_NAME, JoyEmotion.class, "sleep");
        testUpdateEmotionWithAction(AGENT_NAME, SadnessEmotion.class, "wake");
        testUpdateEmotionWithAction(AGENT_NAME, DepressionEmotion.class, "pay");
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithObject() {
        testUpdateEmotionWithObject("other", CompassionEmotion.class, new ObjectProperty("color", "blue"));
        testUpdateEmotionWithObject("other", AdmirationEmotion.class, new ObjectProperty("color", "red"));
        testUpdateEmotionWithObject("other", RejectionEmotion.class, new ObjectProperty("color", "white"));
        testUpdateEmotionWithObject("other", AngerEmotion.class, new ObjectProperty("color", "black"));
        testUpdateEmotionWithObject(AGENT_NAME, HappinessEmotion.class, new ObjectProperty("color", "blue"));
        testUpdateEmotionWithObject(AGENT_NAME, JoyEmotion.class, new ObjectProperty("color", "red"));
        testUpdateEmotionWithObject(AGENT_NAME, SadnessEmotion.class, new ObjectProperty("color", "white"));
        testUpdateEmotionWithObject(AGENT_NAME, DepressionEmotion.class, new ObjectProperty("color", "black"));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithActionAndUpperAgent() {
        String agentName = "AGENT_NAME";
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentName, AGENT_KNOWLEDGE_PATH);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        testUpdateEmotionWithAction(agentName, HappinessEmotion.class, "eat");
        testUpdateEmotionWithAction(agentName, JoyEmotion.class, "sleep");
        testUpdateEmotionWithAction(agentName, SadnessEmotion.class, "wake");
        testUpdateEmotionWithAction(agentName, DepressionEmotion.class, "pay");
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithObjectAndUpperAgent() {
        String agentName = "AGENT_NAME";
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentName, AGENT_KNOWLEDGE_PATH);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        testUpdateEmotionWithObject(agentName, HappinessEmotion.class, new ObjectProperty("color", "blue"));
        testUpdateEmotionWithObject(agentName, JoyEmotion.class, new ObjectProperty("color", "red"));
        testUpdateEmotionWithObject(agentName, SadnessEmotion.class, new ObjectProperty("color", "white"));
        testUpdateEmotionWithObject(agentName, DepressionEmotion.class, new ObjectProperty("color", "black"));
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperAction() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME, AGENT_KNOWLEDGE_PATH);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        behaviouralKnowledgeBase.addTheory("satisfactionAction(AGENT, 'Eat', positive_high) :- self(AGENT).");
        testUpdateEmotionWithAction(AGENT_NAME, HappinessEmotion.class, "Eat");
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperObject() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME, AGENT_KNOWLEDGE_PATH);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        behaviouralKnowledgeBase.addTheory("satisfactionObject(AGENT, PROPERTIES, positive_high) :- member(color='Gray', PROPERTIES).");
        testUpdateEmotionWithObject(AGENT_NAME, HappinessEmotion.class, new ObjectProperty("color", "Gray"));
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