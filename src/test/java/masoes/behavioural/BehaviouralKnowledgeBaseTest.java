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

    private static final String AGENT_NAME = "agentName";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    @Before
    public void setUp() {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(AGENT_NAME, null);
    }

    @Test
    public void shouldSolveCorrectlySelfAndOtherAgent() throws Exception {
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
        testEmotion("other", "greeting", "compassion", "satisfaction(AGENT, greeting, positive_high) :- other(AGENT).");
        testEmotion("other", "smile", "admiration", "satisfaction(AGENT, smile, positive_low) :- other(AGENT).");
        testEmotion("other", "run", "rejection", "satisfaction(AGENT, run, negative_low) :- other(AGENT).");
        testEmotion("other", "bye", "anger", "satisfaction(AGENT, bye, negative_high) :- other(AGENT).");
        testEmotion(AGENT_NAME, "eat", "happiness", "satisfaction(AGENT, eat, positive_high) :- self(AGENT).");
        testEmotion(AGENT_NAME, "sleep", "joy", "satisfaction(AGENT, sleep, positive_low) :- self(AGENT).");
        testEmotion(AGENT_NAME, "wake", "sadness", "satisfaction(AGENT, wake, negative_low) :- self(AGENT).");
        testEmotion(AGENT_NAME, "pay", "depression", "satisfaction(AGENT, pay, negative_high) :- self(AGENT).");
    }

    private void testEmotion(String agent, String action, String expectedEmotion, String theory) throws Exception {
        behaviouralKnowledgeBase.addTheory(theory);
        SolveInfo solve = behaviouralKnowledgeBase.solve("emotion(" + agent + ", " + action + ", X).");
        assertThat(solve.getVarValue("X").toString(), is(expectedEmotion));
    }

}