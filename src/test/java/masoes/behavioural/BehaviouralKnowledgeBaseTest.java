/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BehaviouralKnowledgeBaseTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    @Before
    public void setUp() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME, AGENT_KNOWLEDGE_PATH);
    }

    @Test
    public void shouldSolveCorrectlySelfAndOtherAgent() throws Exception {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME.toUpperCase(), AGENT_KNOWLEDGE_PATH);
        SolveInfo solveSelf = behaviouralKnowledgeBase.solve("self(X).");
        assertThat(solveSelf.getVarValue("X").toString(), is(AGENT_NAME));
        SolveInfo solveOther = behaviouralKnowledgeBase.solve("other(otherAgent).");
        assertThat(solveOther.toString(), is("yes."));
    }

    @Test
    public void shouldSolveCorrectlyBehaviourManagerQuestions() throws Exception {
        testBehaviourPriority("admiration", "imitative");
        testBehaviourPriority("compassion", "imitative");
        testBehaviourPriority("happiness", "imitative");
        testBehaviourPriority("joy", "imitative");
        testBehaviourPriority("rejection", "cognitive");
        testBehaviourPriority("sadness", "cognitive");
        testBehaviourPriority("anger", "reactive");
        testBehaviourPriority("depression", "reactive");
    }

    private void testBehaviourPriority(String emotion, String expectedBehaviour) throws Exception {
        SolveInfo solve = behaviouralKnowledgeBase.solve("behaviourPriority(" + emotion + ", X).");
        assertThat(solve.getVarValue("X").toString(), is(expectedBehaviour));
    }

    @Test
    public void shouldSolveCorrectlyEmotionalConfiguratorQuestions() throws Exception {
        testEmotion("other", "greeting", "compassion");
        testEmotion("other", "smile", "admiration");
        testEmotion("other", "run", "rejection");
        testEmotion("other", "bye", "anger");
        testEmotion(AGENT_NAME, "eat", "happiness");
        testEmotion(AGENT_NAME, "sleep", "joy");
        testEmotion(AGENT_NAME, "wake", "sadness");
        testEmotion(AGENT_NAME, "pay", "depression");
    }

    private void testEmotion(String agent, String action, String expectedEmotion) throws Exception {
        SolveInfo solve = behaviouralKnowledgeBase.solve("emotion(" + agent + ", " + action + ", X).");
        assertThat(solve.getVarValue("X").toString(), is(expectedEmotion));
    }

}