/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import masoes.behavioural.BehaviourType;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.SadnessEmotion;
import ontology.masoes.ActionStimulus;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import test.PowerMockitoTest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class EmotionalAgentLoggerTest extends PowerMockitoTest {

    private static final String EXPECTED_AGENT_NAME = "AGENT";
    private Logger loggerMock;
    private EmotionalAgentLogger emotionalAgentLoggerMock;
    private EmotionalAgent emotionalAgentMock;
    private ACLMessage messageMock;

    @Before
    public void setUp() throws Exception {
        loggerMock = mock(Logger.class);
        emotionalAgentMock = mock(EmotionalAgent.class);
        emotionalAgentLoggerMock = new EmotionalAgentLogger(emotionalAgentMock);
        setFieldValue(emotionalAgentLoggerMock, "logger", loggerMock);

        messageMock = mock(ACLMessage.class);
        doReturn(EXPECTED_AGENT_NAME).when(emotionalAgentMock).getLocalName();
        doReturn("message").when(messageMock).toString();
        doReturn("toString").when(emotionalAgentMock).toString();
    }

    @Test
    public void shouldLogAgentException() {
        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("Exception in agent \"%s\", class \"%s\": %s", EXPECTED_AGENT_NAME, emotionalAgentMock.getClass().getName(), expectedException.getMessage());
        emotionalAgentLoggerMock.exception(expectedException);
        verify(loggerMock).error(expectedMessage, expectedException);
        verify(emotionalAgentMock).handleMessage(Level.ERROR, expectedMessage);
    }

    @Test
    public void shouldLogAgentRequestMessage() {
        String expectedMessage = String.format("Message request in agent \"%s\": %s", EXPECTED_AGENT_NAME, messageMock);
        emotionalAgentLoggerMock.messageRequest(messageMock);
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogAgentResponseMessage() {
        String expectedMessage = String.format("Message response in agent \"%s\": %s", EXPECTED_AGENT_NAME, messageMock);
        emotionalAgentLoggerMock.messageResponse(messageMock);
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogAgent() {
        String expectedMessage = String.format("Agent %s: %s", EXPECTED_AGENT_NAME, emotionalAgentMock);
        emotionalAgentLoggerMock.agentInfo();
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogAgentInfo() {
        String expectedInfo = "expectedInfo";
        String expectedMessage = String.format("Agent %s: %s", EXPECTED_AGENT_NAME, expectedInfo);
        emotionalAgentLoggerMock.info(expectedInfo);
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogUpdatingBehaviour() {
        String expectedMessage = String.format("Actualizando comportamiento %s a %s", BehaviourType.COGNITIVE, BehaviourType.REACTIVE);
        emotionalAgentLoggerMock.updatingBehaviour(BehaviourType.COGNITIVE, BehaviourType.REACTIVE);
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogStartingBehaviour() {
        String expectedMessage = String.format("Inicializando comportamiento %s", BehaviourType.COGNITIVE);
        emotionalAgentLoggerMock.startingBehaviour(BehaviourType.COGNITIVE);
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogUpdatingEmotion() {
        HappinessEmotion happinessEmotion = new HappinessEmotion();
        SadnessEmotion sadnessEmotion = new SadnessEmotion();
        String expectedMessage = String.format("Actualizando emoción %s a %s", happinessEmotion.getName().toUpperCase(), sadnessEmotion.getName().toUpperCase());
        emotionalAgentLoggerMock.updatingEmotion(happinessEmotion, sadnessEmotion);
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogSendingDone() {
        String expectedMessage = "Enviando respuesta: completado";
        emotionalAgentLoggerMock.sendingDone();
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogStimulus() {
        ActionStimulus actionStimulus = new ActionStimulus(new AID("agent", AID.ISGUID), "actionName");
        String expectedMessage = String.format("Recibiendo mensaje: %s", actionStimulus);
        emotionalAgentLoggerMock.receivingStimulus(actionStimulus);
        verify(loggerMock).info(expectedMessage);
        verify(emotionalAgentMock).handleMessage(Level.INFO, expectedMessage);
    }

}