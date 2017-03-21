/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.MessageTemplate;
import masoes.behavioural.BehaviourType;
import masoes.behavioural.BehaviouralComponent;
import masoes.behavioural.EmotionalState;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import ontology.OntologyMatchExpression;
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
import static test.ReflectionTestUtils.setFieldValue;

public class EmotionalAgentBehaviourTest extends PowerMockitoTest {

    private EmotionalAgent emotionalAgentMock;
    private EmotionalAgentBehaviour emotionalAgentBehaviour;
    private BehaviouralComponent behaviouralComponentMock;
    private AID agentAID;

    @Before
    public void setUp() throws Exception {
        emotionalAgentMock = mock(EmotionalAgent.class);
        behaviouralComponentMock = mock(BehaviouralComponent.class);

        agentAID = new AID("agentName", AID.ISGUID);

        doReturn(behaviouralComponentMock).when(emotionalAgentMock).getBehaviouralComponent();
        doReturn(agentAID).when(emotionalAgentMock).getAID();

        emotionalAgentBehaviour = new EmotionalAgentBehaviour(emotionalAgentMock);

        setFieldValue(emotionalAgentBehaviour, "logger", mock(EmotionalAgentLogger.class));
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action actionGetEmotionalState = new Action(new AID(), new GetEmotionalState());
        Action actionEvaluateStimulus = new Action(new AID(), new EvaluateStimulus());
        assertTrue(emotionalAgentBehaviour.isValidAction(actionGetEmotionalState));
        assertTrue(emotionalAgentBehaviour.isValidAction(actionEvaluateStimulus));
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(emotionalAgentBehaviour.getOntology(), is(instanceOf(MasoesOntology.class)));
        assertReflectionEquals(new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), emotionalAgentBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldEvaluateActionStimulus() throws Exception {
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus();
        ActionStimulus stimulus = new ActionStimulus();
        stimulus.setActionName("expectedActionName");
        stimulus.setActor(new AID("expectedActorName", AID.ISGUID));
        evaluateStimulus.setStimulus(stimulus);

        Action action = new Action();
        action.setAction(evaluateStimulus);

        Predicate predicate = emotionalAgentBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(Done.class)));

        Done done = (Done) predicate;
        assertThat(done.getAction(), is(action));

        verify(behaviouralComponentMock).evaluateStimulus(stimulus);
    }

    @Test
    public void shouldEvaluateObjectStimulus() throws Exception {
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus();
        ObjectStimulus stimulus = new ObjectStimulus();
        stimulus.setObjectName("expectedObjectName");
        stimulus.setCreator(new AID("expectedCreatorName", AID.ISGUID));
        evaluateStimulus.setStimulus(stimulus);

        Action action = new Action();
        action.setAction(evaluateStimulus);

        Predicate predicate = emotionalAgentBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(Done.class)));

        Done done = (Done) predicate;
        assertThat(done.getAction(), is(action));

        verify(behaviouralComponentMock).evaluateStimulus(stimulus);
    }

    @Test
    public void shouldEvaluateEventStimulus() throws Exception {
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus();
        EventStimulus stimulus = new EventStimulus();
        stimulus.setEventName("expectedEventName");
        stimulus.setAffected(new AID("expectedAffectedName", AID.ISGUID));
        evaluateStimulus.setStimulus(stimulus);

        Action action = new Action();
        action.setAction(evaluateStimulus);

        Predicate predicate = emotionalAgentBehaviour.performAction(action);
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
        doReturn(emotionalState).when(behaviouralComponentMock).getCurrentEmotionalState();

        doReturn(BehaviourType.IMITATIVE).when(behaviouralComponentMock).getCurrentBehaviourType();

        Action action = new Action();
        action.setAction(new GetEmotionalState());
        Predicate predicate = emotionalAgentBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(AgentState.class)));

        AgentState agentState = (AgentState) predicate;
        assertThat(agentState.getAgent(), is(agentAID));

        assertThat(agentState.getBehaviourState().getType(), is(BehaviourType.IMITATIVE.toString()));

        assertThat(agentState.getEmotionState().getName(), is(emotion.getName()));
        assertThat(agentState.getEmotionState().getType(), is(emotion.getType().toString()));
        assertThat(agentState.getEmotionState().getActivation(), is(emotionalState.getActivation()));
        assertThat(agentState.getEmotionState().getSatisfaction(), is(emotionalState.getSatisfaction()));
        assertThat(agentState.getEmotionState().getClassName(), is(emotion.getClass().getSimpleName()));
    }

}