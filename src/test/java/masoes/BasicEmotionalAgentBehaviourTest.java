/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import environment.dummy.DummyCognitiveBehaviour;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.MessageTemplate;
import masoes.behavioural.BehaviouralComponent;
import masoes.behavioural.EmotionalState;
import masoes.behavioural.emotion.HappinessEmotion;
import ontology.OntologyMatchExpression;
import ontology.masoes.AgentState;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;
import ontology.masoes.Stimulus;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class BasicEmotionalAgentBehaviourTest extends PowerMockitoTest {

    private EmotionalAgent emotionalAgentMock;
    private BasicEmotionalAgentBehaviour basicEmotionalAgentBehaviour;
    private BehaviouralComponent behaviouralComponentMock;
    private AID agentAID;

    @Before
    public void setUp() {
        emotionalAgentMock = mock(EmotionalAgent.class);
        behaviouralComponentMock = mock(BehaviouralComponent.class);

        agentAID = new AID("agentName", AID.ISGUID);

        doReturn(behaviouralComponentMock).when(emotionalAgentMock).getBehaviouralComponent();
        doReturn(agentAID).when(emotionalAgentMock).getAID();

        basicEmotionalAgentBehaviour = new BasicEmotionalAgentBehaviour(emotionalAgentMock);
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action actionGetEmotionalState = new Action(new AID(), new GetEmotionalState());
        Action actionEvaluateStimulus = new Action(new AID(), new EvaluateStimulus());
        assertTrue(basicEmotionalAgentBehaviour.isValidAction(actionGetEmotionalState));
        assertTrue(basicEmotionalAgentBehaviour.isValidAction(actionEvaluateStimulus));
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(basicEmotionalAgentBehaviour.getOntology(), is(instanceOf(MasoesOntology.class)));
        assertReflectionEquals(new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), basicEmotionalAgentBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldEvaluateStimulus() throws Exception {
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus();
        Stimulus stimulus = new Stimulus();
        evaluateStimulus.setStimulus(stimulus);

        Action action = new Action();
        action.setAction(evaluateStimulus);

        Predicate predicate = basicEmotionalAgentBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(Done.class)));

        Done done = (Done) predicate;
        assertThat(done.getAction(), is(action));

        verify(behaviouralComponentMock).evaluateStimulus(stimulus);
    }

    @Test
    public void shouldReturnAgentStatus() throws Exception {
        HappinessEmotion emotion = new HappinessEmotion();
        doReturn(emotion).when(behaviouralComponentMock).getCurrentEmotion();

        EmotionalState emotionalState = emotion.getRandomEmotionalState();
        doReturn(emotionalState).when(behaviouralComponentMock).getEmotionalState();

        DummyCognitiveBehaviour behaviour = new DummyCognitiveBehaviour();
        doReturn(behaviour).when(behaviouralComponentMock).getCurrentEmotionalBehaviour();

        Action action = new Action();
        action.setAction(new GetEmotionalState());
        Predicate predicate = basicEmotionalAgentBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(AgentState.class)));

        AgentState agentState = (AgentState) predicate;
        assertThat(agentState.getAgent(), is(agentAID));

        assertThat(agentState.getBehaviourState().getName(), is(behaviour.getName()));
        assertThat(agentState.getBehaviourState().getType(), is(behaviour.getType().toString()));
        assertThat(agentState.getBehaviourState().getClassName(), is(behaviour.getClass().getSimpleName()));

        assertThat(agentState.getEmotionState().getName(), is(emotion.getName()));
        assertThat(agentState.getEmotionState().getType(), is(emotion.getType().toString()));
        assertThat(agentState.getEmotionState().getActivation(), is(emotionalState.getActivation()));
        assertThat(agentState.getEmotionState().getSatisfaction(), is(emotionalState.getSatisfaction()));
        assertThat(agentState.getEmotionState().getClassName(), is(emotion.getClass().getSimpleName()));
    }

}