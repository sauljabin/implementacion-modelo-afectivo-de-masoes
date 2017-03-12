/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
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
import jade.util.leap.ArrayList;
import masoes.behavioural.BehaviouralComponent;
import masoes.behavioural.EmotionalState;
import masoes.behavioural.emotion.HappinessEmotion;
import ontology.OntologyMatchExpression;
import ontology.masoes.ActionStimulus;
import ontology.masoes.AgentState;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
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
    private EmotionalAgentLogger loggerMock;

    @Before
    public void setUp() {
        emotionalAgentMock = mock(EmotionalAgent.class);
        behaviouralComponentMock = mock(BehaviouralComponent.class);
        loggerMock = mock(EmotionalAgentLogger.class);

        agentAID = new AID("agentName", AID.ISGUID);

        doReturn(behaviouralComponentMock).when(emotionalAgentMock).getBehaviouralComponent();
        doReturn(agentAID).when(emotionalAgentMock).getAID();
        doReturn(loggerMock).when(emotionalAgentMock).getLogger();

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
    public void shouldEvaluateActionStimulus() throws Exception {
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus();
        ActionStimulus stimulus = new ActionStimulus();
        stimulus.setActionName("expectedActionName");
        stimulus.setActor(new AID("expectedActorName", AID.ISGUID));
        evaluateStimulus.setStimulus(stimulus);

        Action action = new Action();
        action.setAction(evaluateStimulus);

        Predicate predicate = basicEmotionalAgentBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(Done.class)));

        Done done = (Done) predicate;
        assertThat(done.getAction(), is(action));

        verify(behaviouralComponentMock).evaluateStimulus(stimulus);
        verify(loggerMock).receivingStimulus(stimulus);
        verify(loggerMock).sendingDone();
    }

    @Test
    public void shouldEvaluateObjectStimulus() throws Exception {
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus();
        ObjectStimulus stimulus = new ObjectStimulus();
        stimulus.setObjectName("expectedActionName");
        stimulus.setCreator(new AID("expectedActorName", AID.ISGUID));

        ArrayList objectProperties = new ArrayList();
        objectProperties.add(new ObjectProperty("name", "value"));
        objectProperties.add(new ObjectProperty("name2", "value2"));
        stimulus.setObjectProperties(objectProperties);
        evaluateStimulus.setStimulus(stimulus);

        Action action = new Action();
        action.setAction(evaluateStimulus);

        Predicate predicate = basicEmotionalAgentBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(Done.class)));

        Done done = (Done) predicate;
        assertThat(done.getAction(), is(action));

        verify(behaviouralComponentMock).evaluateStimulus(stimulus);
        verify(loggerMock).receivingStimulus(stimulus);
        verify(loggerMock).sendingDone();
    }

    @Test
    public void shouldReturnAgentStatus() throws Exception {
        HappinessEmotion emotion = new HappinessEmotion();
        doReturn(emotion).when(behaviouralComponentMock).getCurrentEmotion();

        EmotionalState emotionalState = emotion.getRandomEmotionalState();
        doReturn(emotionalState).when(behaviouralComponentMock).getCurrentEmotionalState();

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