/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import logger.LoggerHandler;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import test.PowerMockitoTest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class AgentLoggerTest extends PowerMockitoTest {

    private static final String EXPECTED_AGENT_NAME = "AGENT";
    private Logger loggerMock;
    private AgentLogger agentLogger;
    private Agent agentMock;
    private ACLMessage messageMock;
    private LoggerHandler loggerHandlerMock;

    @Before
    public void setUp() throws Exception {
        loggerMock = mock(Logger.class);
        agentMock = mock(Agent.class);
        agentLogger = new AgentLogger(agentMock);
        loggerHandlerMock = mock(LoggerHandler.class);
        setFieldValue(agentLogger, "logger", loggerMock);
        setFieldValue(agentLogger, "loggerHandler", loggerHandlerMock);

        messageMock = mock(ACLMessage.class);
        doReturn(EXPECTED_AGENT_NAME).when(agentMock).getLocalName();
        doReturn("message").when(messageMock).toString();
        doReturn("toString").when(agentMock).toString();
    }

    @Test
    public void shouldLogAgentException() {
        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("Exception in agent \"%s\", class \"%s\": %s", EXPECTED_AGENT_NAME, agentMock.getClass().getName(), expectedException.getMessage());
        agentLogger.exception(expectedException);
        verify(loggerMock).error(expectedMessage, expectedException);
        verify(loggerHandlerMock).handleMessage(Level.ERROR, expectedMessage);
    }

    @Test
    public void shouldLogAgentRequestMessage() {
        String expectedMessage = String.format("Message request in agent \"%s\": %s", EXPECTED_AGENT_NAME, messageMock);
        agentLogger.messageRequest(messageMock);
        verify(loggerMock).info(expectedMessage);
        verify(loggerHandlerMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogAgentResponseMessage() {
        String expectedMessage = String.format("Message response in agent \"%s\": %s", EXPECTED_AGENT_NAME, messageMock);
        agentLogger.messageResponse(messageMock);
        verify(loggerMock).info(expectedMessage);
        verify(loggerHandlerMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogAgent() {
        String expectedMessage = String.format("Agent %s: %s", EXPECTED_AGENT_NAME, agentMock);
        agentLogger.agentInfo();
        verify(loggerMock).info(expectedMessage);
        verify(loggerHandlerMock).handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldLogAgentInfo() {
        String expectedInfo = "expectedInfo";
        String expectedMessage = String.format("Agent %s: %s", EXPECTED_AGENT_NAME, expectedInfo);
        agentLogger.info(expectedInfo);
        verify(loggerMock).info(expectedMessage);
        verify(loggerHandlerMock).handleMessage(Level.INFO, expectedMessage);
    }

}