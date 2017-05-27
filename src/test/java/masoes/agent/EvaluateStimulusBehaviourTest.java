/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.MessageTemplate;
import masoes.component.behavioural.BehaviourType;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.component.behavioural.EmotionalState;
import masoes.component.behavioural.emotion.HappinessEmotion;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import ontology.ActionMatchExpression;
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

public class EvaluateStimulusBehaviourTest extends PowerMockitoTest {

    private EvaluateStimulusBehaviour actionResponderBehaviour;
    private EmotionalAgent agentMock;

    private BehaviouralComponent behaviouralComponentMock;
    private AID agentAID;
    private HappinessEmotion emotion;
    private EmotionalState emotionalState;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(EmotionalAgent.class);
        behaviouralComponentMock = mock(BehaviouralComponent.class);

        agentAID = new AID("agentName", AID.ISGUID);

        doReturn(behaviouralComponentMock).when(agentMock).getBehaviouralComponent();
        doReturn(agentAID).when(agentMock).getAID();

        actionResponderBehaviour = new EvaluateStimulusBehaviour(agentMock);

        setFieldValue(actionResponderBehaviour, "logger", mock(EmotionalAgentLogger.class));

        emotion = new HappinessEmotion();
        doReturn(emotion).when(behaviouralComponentMock).getCurrentEmotion();

        emotionalState = emotion.getRandomEmotionalState();
        doReturn(emotionalState).when(behaviouralComponentMock).getCurrentEmotionalState();

        doReturn(BehaviourType.IMITATIVE).when(behaviouralComponentMock).getCurrentBehaviourType();
    }

    @Test
    public void shouldReturnCorrectOntologyTemplateMatch() {
        MessageTemplate expected = new MessageTemplate(new ActionMatchExpression(MasoesOntology.getInstance(), EvaluateStimulus.class));
        assertReflectionEquals(expected, actionResponderBehaviour.getMessageTemplate());
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

        Predicate predicate = actionResponderBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(AgentState.class)));

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

        Predicate predicate = actionResponderBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(AgentState.class)));

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

        Predicate predicate = actionResponderBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(AgentState.class)));

        verify(behaviouralComponentMock).evaluateStimulus(stimulus);
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action actionEvaluateStimulus = new Action(new AID(), new EvaluateStimulus());
        assertTrue(actionResponderBehaviour.isValidAction(actionEvaluateStimulus));
    }

}