/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behaviour;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import logger.jade.JadeLogger;
import masoes.model.Emotion;
import masoes.model.EmotionalAgent;
import masoes.model.EmotionalState;
import masoes.ontology.AgentStatus;
import masoes.ontology.EmotionStatus;
import masoes.ontology.GetAgentStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Agent.class, Behaviour.class})
public class ResponseAgentStatusBehaviourTest {

    private static final String EMOTION_NAME = "EMOTION NAME";
    private static final String BEHAVIOUR_NAME = "BEHAVIOUR NAME";
    private EmotionalAgent emotionalAgentMock;
    private ResponseAgentStatusBehaviour responseAgentStatusBehaviour;
    private JadeLogger mockLogger;
    private Emotion emotionMock;
    private Behaviour behaviourMock;

    @Before
    public void setUp() throws Exception {
        emotionalAgentMock = mock(EmotionalAgent.class);
        responseAgentStatusBehaviour = new ResponseAgentStatusBehaviour(emotionalAgentMock);

        emotionMock = mock(Emotion.class);
        doReturn(EMOTION_NAME).when(emotionMock).getEmotionName();
        doReturn(emotionMock).when(emotionalAgentMock).getCurrentEmotion();

        behaviourMock = mock(Behaviour.class);
        doReturn(BEHAVIOUR_NAME).when(behaviourMock).getBehaviourName();
        doReturn(behaviourMock).when(emotionalAgentMock).getCurrentEmotionalBehaviour();

        mockLogger = mock(JadeLogger.class);
        setFieldValue(responseAgentStatusBehaviour, "logger", mockLogger);
    }

    @Test
    public void shouldReturnAgentStatus() {
        AID aid = new AID();
        doReturn(aid).when(emotionalAgentMock).getAID();

        EmotionalState emotionalState = new EmotionalState();
        doReturn(emotionalState).when(emotionalAgentMock).getEmotionalState();

        GetAgentStatus getAgentStatus = new GetAgentStatus();
        Action action = new Action(aid, getAgentStatus);

        Predicate agentStatus = responseAgentStatusBehaviour.performAction(action);

        AgentStatus expectedAgentStatus = new AgentStatus();
        expectedAgentStatus.setBehaviourName(BEHAVIOUR_NAME);
        expectedAgentStatus.setEmotionName(EMOTION_NAME);
        expectedAgentStatus.setAgent(aid);
        expectedAgentStatus.setEmotionStatus(new EmotionStatus(emotionalState.getActivation(), emotionalState.getSatisfaction()));

        assertReflectionEquals(expectedAgentStatus, agentStatus);
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action action = new Action(new AID(), new GetAgentStatus());
        assertTrue(responseAgentStatusBehaviour.isValidAction(action));
    }

}