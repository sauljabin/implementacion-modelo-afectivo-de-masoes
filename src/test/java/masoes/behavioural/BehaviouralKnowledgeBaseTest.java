/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.Knowledge;
import masoes.agent.EmotionalAgent;
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
    private static final String AGENT_KNOWLEDGE_PATH = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";
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
        SolveInfo solveOther = behaviouralKnowledgeBase.solve("other('otherAgent').");
        assertThat(solveOther.toString(), is("yes."));
    }

    @Test
    public void shouldSolveCorrectlyAnyoneAgent() throws Exception {
        SolveInfo solveSelf = behaviouralKnowledgeBase.solve("anyone('" + AGENT_NAME + "').");
        assertThat(solveSelf.toString(), is("yes."));
        SolveInfo solveOther = behaviouralKnowledgeBase.solve("anyone('otherAgent').");
        assertThat(solveOther.toString(), is("yes."));
    }

    @Test
    public void shouldSolveSelfAgentWithUpperName() throws Exception {
        String nameUpper = "NAME_UPPER";
        doReturn(nameUpper).when(agentMock).getLocalName();
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentMock);
        SolveInfo solveSelf = behaviouralKnowledgeBase.solve("self('" + nameUpper + "').");
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
        SolveInfo solve = behaviouralKnowledgeBase.solve("behaviourPriority('" + emotion + "', X).");
        assertThat(solve.getVarValue("X").toString(), is(expectedBehaviour));
    }

    @Test
    public void shouldSolveCorrectlyEmotionalConfiguratorQuestionsWithAction() throws Exception {
        testEmotionByAction("other", "greeting", "negative", "positive");
        testEmotionByAction("other", "smile", "positive", "negative");
        testEmotionByAction("other", "run", "negative", "negative");
        testEmotionByAction("other", "bye", "negative", "negative");
        testEmotionByAction(AGENT_NAME, "eat", "positive", "positive");
        testEmotionByAction(AGENT_NAME, "sleep", "positive", "positive");
        testEmotionByAction(AGENT_NAME, "wake", "negative", "negative");
        testEmotionByAction(AGENT_NAME, "pay", "negative", "negative");
    }

    private void testEmotionByAction(String agent, String parameter, String expectedActivation, String expectedSatisfaction) throws Exception {
        SolveInfo solve = behaviouralKnowledgeBase.solve("valence('" + agent + "', '" + parameter + "', X, Y).");
        assertThat(solve.getVarValue("X").toString(), is(expectedActivation));
        assertThat(solve.getVarValue("Y").toString(), is(expectedSatisfaction));
    }

}