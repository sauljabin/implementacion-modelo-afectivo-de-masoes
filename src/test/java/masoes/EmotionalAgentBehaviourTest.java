/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import masoes.conductual.Emotion;
import masoes.conductual.EmotionalState;
import ontology.masoes.AgentStatus;
import ontology.masoes.EmotionStatus;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetAgentStatus;
import ontology.masoes.MasoesOntology;
import ontology.masoes.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Agent.class, Behaviour.class})
public class EmotionalAgentBehaviourTest {

    private static final String EMOTION_NAME = "EMOTION NAME";
    private static final String BEHAVIOUR_NAME = "BEHAVIOUR NAME";
    private EmotionalAgent emotionalAgentMock;
    private EmotionalAgentBehaviour emotionalAgentBehaviour;
    private Emotion emotionMock;
    private Behaviour behaviourMock;

    @Before
    public void setUp() {
        emotionalAgentMock = mock(EmotionalAgent.class);
        emotionalAgentBehaviour = new EmotionalAgentBehaviour(emotionalAgentMock);

        emotionMock = mock(Emotion.class);
        doReturn(EMOTION_NAME).when(emotionMock).getEmotionName();
        doReturn(emotionMock).when(emotionalAgentMock).getCurrentEmotion();

        behaviourMock = mock(Behaviour.class);
        doReturn(BEHAVIOUR_NAME).when(behaviourMock).getBehaviourName();
        doReturn(behaviourMock).when(emotionalAgentMock).getCurrentEmotionalBehaviour();
    }

    @Test
    public void shouldReturnAgentStatus() throws Exception {
        AID aid = new AID();
        doReturn(aid).when(emotionalAgentMock).getAID();

        EmotionalState emotionalState = new EmotionalState();
        doReturn(emotionalState).when(emotionalAgentMock).getEmotionalState();

        GetAgentStatus getAgentStatus = new GetAgentStatus();
        Action action = new Action(aid, getAgentStatus);

        Predicate agentStatus = emotionalAgentBehaviour.performAction(action);

        AgentStatus expectedAgentStatus = new AgentStatus();
        expectedAgentStatus.setBehaviourName(BEHAVIOUR_NAME);
        expectedAgentStatus.setEmotionName(EMOTION_NAME);
        expectedAgentStatus.setAgent(aid);
        expectedAgentStatus.setEmotionStatus(new EmotionStatus(emotionalState.getActivation(), emotionalState.getSatisfaction()));

        assertReflectionEquals(expectedAgentStatus, agentStatus);
    }

    @Test
    public void shouldReturnValidGetAgentStatus() {
        Action action = new Action(new AID(), new GetAgentStatus());
        assertTrue(emotionalAgentBehaviour.isValidAction(action));
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(emotionalAgentBehaviour.getOntology(), is(instanceOf(MasoesOntology.class)));
        assertReflectionEquals(new MessageTemplate(new MasoesMatchExpression()), emotionalAgentBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldEvaluateStimulus() throws Exception {
        Stimulus stimulus = new Stimulus();

        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);
        Action action = new Action(new AID(), evaluateStimulus);

        Predicate predicate = emotionalAgentBehaviour.performAction(action);
        verify(emotionalAgentMock).evaluateStimulus(any());
        assertThat(predicate, is(instanceOf(Done.class)));
    }

    @Test
    public void shouldReturnValidEvaluateStimulus() {
        Action action = new Action(new AID(), new EvaluateStimulus());
        assertTrue(emotionalAgentBehaviour.isValidAction(action));
    }

}