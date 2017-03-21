/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import masoes.component.behavioural.BehaviourType;
import masoes.component.behavioural.emotion.HappinessEmotion;
import masoes.component.behavioural.emotion.SadnessEmotion;
import masoes.ontology.stimulus.ActionStimulus;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import test.PowerMockitoTest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
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
        emotionalAgentLoggerMock.exception(expectedException);
        verify(loggerMock).error(contains("error"), eq(expectedException));
    }

    @Test
    public void shouldLogAgentRequestMessage() {
        emotionalAgentLoggerMock.messageRequest(messageMock);
        verify(loggerMock).info(contains(messageMock.toString()));
    }

    @Test
    public void shouldLogAgentResponseMessage() {
        emotionalAgentLoggerMock.messageResponse(messageMock);
        verify(loggerMock).info(contains(messageMock.toString()));
    }

    @Test
    public void shouldLogAgent() {
        emotionalAgentLoggerMock.agentInfo();
        verify(loggerMock).info(contains(emotionalAgentMock.toString()));
    }

    @Test
    public void shouldLogAgentInfo() {
        String expectedInfo = "expectedInfo";
        emotionalAgentLoggerMock.info(expectedInfo);
        verify(loggerMock).info(contains(expectedInfo));
    }

    @Test
    public void shouldLogUpdatingBehaviour() {
        emotionalAgentLoggerMock.updatingBehaviour(BehaviourType.COGNITIVE, BehaviourType.REACTIVE);
        verify(loggerMock).info(anyString());
    }

    @Test
    public void shouldLogStartingBehaviour() {
        emotionalAgentLoggerMock.startingBehaviour(BehaviourType.COGNITIVE);
        verify(loggerMock).info(anyString());
    }

    @Test
    public void shouldLogUpdatingEmotion() {
        HappinessEmotion happinessEmotion = new HappinessEmotion();
        SadnessEmotion sadnessEmotion = new SadnessEmotion();
        emotionalAgentLoggerMock.updatingEmotion(happinessEmotion, sadnessEmotion);
        verify(loggerMock).info(anyString());
    }

    @Test
    public void shouldLogSendingDone() {
        emotionalAgentLoggerMock.sendingDone();
        verify(loggerMock).info(anyString());
    }

    @Test
    public void shouldLogStimulus() {
        ActionStimulus actionStimulus = new ActionStimulus(new AID("agent", AID.ISGUID), "actionName");
        emotionalAgentLoggerMock.receivingStimulus(actionStimulus);
        verify(loggerMock).info(contains(actionStimulus.toString()));
    }

}