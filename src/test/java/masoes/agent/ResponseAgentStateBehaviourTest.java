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
import masoes.ontology.state.GetEmotionalState;
import ontology.ActionMatchExpression;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ResponseAgentStateBehaviourTest extends PowerMockitoTest {

    private ResponseAgentStateBehaviour actionResponderBehaviour;
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

        actionResponderBehaviour = new ResponseAgentStateBehaviour(agentMock);

        emotion = new HappinessEmotion();
        doReturn(emotion).when(behaviouralComponentMock).getCurrentEmotion();

        emotionalState = emotion.getRandomEmotionalState();
        doReturn(emotionalState).when(behaviouralComponentMock).getCurrentEmotionalState();

        doReturn(BehaviourType.IMITATIVE).when(behaviouralComponentMock).getCurrentBehaviourType();
    }

    @Test
    public void shouldReturnCorrectOntologyTemplateMatch() {
        MessageTemplate expected = new MessageTemplate(new ActionMatchExpression(MasoesOntology.getInstance(), GetEmotionalState.class));
        assertReflectionEquals(expected, actionResponderBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldReturnAgentStatus() throws Exception {
        Action action = new Action();
        action.setAction(new GetEmotionalState());
        Predicate predicate = actionResponderBehaviour.performAction(action);
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

    @Test
    public void shouldReturnValidAgentAction() {
        Action actionEvaluateStimulus = new Action(new AID(), new GetEmotionalState());
        assertTrue(actionResponderBehaviour.isValidAction(actionEvaluateStimulus));
    }

}