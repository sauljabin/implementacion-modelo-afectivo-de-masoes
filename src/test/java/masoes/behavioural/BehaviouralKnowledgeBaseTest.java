/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.Knowledge;
import masoes.EmotionalAgent;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

public class BehaviouralKnowledgeBaseTest extends PowerMockitoTest {

    private static final String AGENT_NAME = "agent";
    private static final String AGENT_KNOWLEDGE_PATH = "theories/dummy/dummyEmotionalAgent.prolog";
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private EmotionalAgent agentMock;

    @Before
    public void setUp() {
        agentMock = mock(EmotionalAgent.class);
        doReturn(new Knowledge(Paths.get(AGENT_KNOWLEDGE_PATH))).when(agentMock).getKnowledge();
        doReturn(AGENT_NAME).when(agentMock).getLocalName();
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
    }

    @Test
    public void shouldSolveCorrectlySelfAndOtherAgent() throws Exception {
        SolveInfo solveSelf = behaviouralKnowledgeBase.solve("self(X).");
        assertThat(solveSelf.getVarValue("X").toString(), is(AGENT_NAME));
        SolveInfo solveOther = behaviouralKnowledgeBase.solve("other(otherAgent).");
        assertThat(solveOther.toString(), is("yes."));
    }

    @Test
    public void shouldSolveCorrectlyAnyoneAgent() throws Exception {
        SolveInfo solveSelf = behaviouralKnowledgeBase.solve("anyone(" + AGENT_NAME + ").");
        assertThat(solveSelf.toString(), is("yes."));
        SolveInfo solveOther = behaviouralKnowledgeBase.solve("anyone(otherAgent).");
        assertThat(solveOther.toString(), is("yes."));
    }

    @Test
    public void shouldSolveSelfAgentWithUpperName() throws Exception {
        String nameUpper = "NAME_UPPER";
        doReturn(nameUpper).when(agentMock).getLocalName();
        SolveInfo solveSelf = behaviouralKnowledgeBase.solve("self(" + nameUpper + ").");
        assertThat(solveSelf.toString(), containsString("yes."));
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
    public void shouldSolveCorrectlyEmotionalConfiguratorQuestionsWithAction() throws Exception {
        testEmotion("other", "greeting", "compassion");
        testEmotion("other", "smile", "admiration");
        testEmotion("other", "run", "rejection");
        testEmotion("other", "bye", "anger");
        testEmotion(AGENT_NAME, "eat", "happiness");
        testEmotion(AGENT_NAME, "sleep", "joy");
        testEmotion(AGENT_NAME, "wake", "sadness");
        testEmotion(AGENT_NAME, "pay", "depression");
    }

    @Test
    public void shouldSolveCorrectlyEmotionalConfiguratorQuestionsWithObject() throws Exception {
        testEmotion("other", "[color=blue]", "compassion");
        testEmotion("other", "[color=red]", "admiration");
        testEmotion("other", "[color=white]", "rejection");
        testEmotion("other", "[color=black]", "anger");
        testEmotion(AGENT_NAME, "[color=blue]", "happiness");
        testEmotion(AGENT_NAME, "[color=red]", "joy");
        testEmotion(AGENT_NAME, "[color=white]", "sadness");
        testEmotion(AGENT_NAME, "[color=black]", "depression");
    }

    private void testEmotion(String agent, String parameter, String expectedEmotion) throws Exception {
        SolveInfo solve = behaviouralKnowledgeBase.solve("emotion(" + agent + ", " + parameter + ", X).");
        assertThat(solve.getVarValue("X").toString(), is(expectedEmotion));
    }

}