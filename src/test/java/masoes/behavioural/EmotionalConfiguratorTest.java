/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import jade.core.AID;
import masoes.behavioural.emotion.AdmirationEmotion;
import masoes.behavioural.emotion.AngerEmotion;
import masoes.behavioural.emotion.CompassionEmotion;
import masoes.behavioural.emotion.DepressionEmotion;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.JoyEmotion;
import masoes.behavioural.emotion.RejectionEmotion;
import masoes.behavioural.emotion.SadnessEmotion;
import ontology.masoes.Stimulus;
import org.junit.Before;
import org.junit.Test;

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
    public void shouldUpdateCorrectlyTheEmotion() {
        testUpdateEmotion("greeting", CompassionEmotion.class, "other");
        testUpdateEmotion("smile", AdmirationEmotion.class, "other");
        testUpdateEmotion("run", RejectionEmotion.class, "other");
        testUpdateEmotion("bye", AngerEmotion.class, "other");
        testUpdateEmotion("eat", HappinessEmotion.class, AGENT_NAME);
        testUpdateEmotion("sleep", JoyEmotion.class, AGENT_NAME);
        testUpdateEmotion("wake", SadnessEmotion.class, AGENT_NAME);
        testUpdateEmotion("pay", DepressionEmotion.class, AGENT_NAME);
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperAgent() {
        String agentName = "AGENT_NAME";
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentName, AGENT_KNOWLEDGE_PATH);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        testUpdateEmotion("eat", HappinessEmotion.class, agentName);
        testUpdateEmotion("sleep", JoyEmotion.class, agentName);
        testUpdateEmotion("wake", SadnessEmotion.class, agentName);
        testUpdateEmotion("pay", DepressionEmotion.class, agentName);
    }

    @Test
    public void shouldUpdateCorrectlyTheEmotionWithUpperAction() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME, AGENT_KNOWLEDGE_PATH);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        behaviouralKnowledgeBase.addTheory("satisfaction(AGENT, 'Eat', positive_high) :- self(AGENT).");
        testUpdateEmotion("Eat", HappinessEmotion.class, AGENT_NAME);
    }

    private void testUpdateEmotion(String actionName, Class<? extends Emotion> expectedEmotion, String agentName) {
        emotionalConfigurator.updateEmotion(new Stimulus(new AID(agentName, AID.ISGUID), actionName));
        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(expectedEmotion)));
        assertTrue(emotionalConfigurator.getEmotion().getGeometry().intersects(emotionalConfigurator.getEmotionalState().toPoint()));
    }

}