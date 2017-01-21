/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger.jade;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.model.Emotion;
import masoes.model.EmotionalAgent;
import masoes.model.EmotionalState;
import masoes.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Agent.class, Behaviour.class})
public class JadeLoggerTest {

    private static final String EXPECTED_AGENT_NAME = "AGENT";
    private Logger loggerMock;
    private JadeLogger jadeLogger;
    private EmotionalAgent agentMock;
    private Behaviour behaviourMock;

    @Before
    public void setUp() {
        loggerMock = mock(Logger.class);
        agentMock = mock(EmotionalAgent.class);
        behaviourMock = mock(Behaviour.class);
        jadeLogger = new JadeLogger(loggerMock);
        doReturn(EXPECTED_AGENT_NAME).when(agentMock).getLocalName();
    }

    @Test
    public void shouldLogAgentException() {
        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("Exception in agent \"%s\": %s", EXPECTED_AGENT_NAME, expectedException.getMessage());
        jadeLogger.agentException(agentMock, expectedException);
        verify(loggerMock).error(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldLogAgentMessage() {
        ACLMessage messageMock = mock(ACLMessage.class);
        doReturn("message").when(messageMock).toString();
        String expectedMessage = String.format("Message in agent \"%s\": %s", EXPECTED_AGENT_NAME, messageMock);
        jadeLogger.agentMessage(agentMock, messageMock);
        verify(loggerMock).info(eq(expectedMessage));
    }

    @Test
    public void shouldLogAgentEmotionalState() {
        Emotion emotionMock = mock(Emotion.class);
        doReturn("emotion").when(emotionMock).toString();
        doReturn(emotionMock).when(agentMock).getCurrentEmotion();
        EmotionalState emotionalState = new EmotionalState();
        doReturn(emotionalState).when(agentMock).getEmotionalState();

        String expectedBehaviourName = "behaviour";
        doReturn(expectedBehaviourName).when(behaviourMock).getBehaviourName();
        doReturn(behaviourMock).when(agentMock).getCurrentEmotionalBehaviour();

        String expectedMessage = String.format("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, emotionMock, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalState(agentMock);
        verify(loggerMock).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        doReturn(null).when(agentMock).getCurrentEmotionalBehaviour();
        expectedMessage = String.format("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, emotionMock, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalState(agentMock);
        verify(loggerMock).info(eq(expectedMessage));
    }

    @Test
    public void shouldLogAgentEmotionalStateChanged() {
        Stimulus stimulusMock = mock(Stimulus.class);
        doReturn("stimulus").when(stimulusMock).toString();

        Emotion emotionMock = mock(Emotion.class);
        doReturn("emotion").when(emotionMock).toString();
        doReturn(emotionMock).when(agentMock).getCurrentEmotion();

        EmotionalState emotionalState = new EmotionalState();
        doReturn(emotionalState).when(agentMock).getEmotionalState();

        String expectedBehaviourName = "behaviour";
        doReturn(expectedBehaviourName).when(behaviourMock).getBehaviourName();
        doReturn(behaviourMock).when(agentMock).getCurrentEmotionalBehaviour();

        String expectedMessage = String.format("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, stimulusMock, emotionMock, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalStateChanged(agentMock, stimulusMock);
        verify(loggerMock).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        doReturn(null).when(agentMock).getCurrentEmotionalBehaviour();
        expectedMessage = String.format("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, stimulusMock, emotionMock, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalStateChanged(agentMock, stimulusMock);
        verify(loggerMock).info(eq(expectedMessage));
    }

}