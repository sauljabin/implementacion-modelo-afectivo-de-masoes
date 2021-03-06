/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import test.PowerMockitoTest;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
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

    @Before
    public void setUp() throws Exception {
        loggerMock = mock(Logger.class);
        agentMock = mock(Agent.class);
        agentLogger = new AgentLogger(agentMock);
        setFieldValue(agentLogger, "logger", loggerMock);

        messageMock = mock(ACLMessage.class);
        doReturn(EXPECTED_AGENT_NAME).when(agentMock).getLocalName();
        doReturn("message").when(messageMock).toString();
        doReturn("toString").when(agentMock).toString();
    }

    @Test
    public void shouldLogAgentException() {
        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("\"%s\": %s", agentMock.getClass().getName(), expectedException.getMessage());
        agentLogger.exception(expectedException);
        verify(loggerMock).error(contains(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldLogAgentRequestMessage() {
        String expectedMessage = String.format("\"%s\": %s", EXPECTED_AGENT_NAME, messageMock);
        agentLogger.messageRequest(messageMock);
        verify(loggerMock).info(contains(expectedMessage));
    }

    @Test
    public void shouldLogAgentResponseMessage() {
        String expectedMessage = String.format("\"%s\": %s", EXPECTED_AGENT_NAME, messageMock);
        agentLogger.messageResponse(messageMock);
        verify(loggerMock).info(contains(expectedMessage));
    }

    @Test
    public void shouldLogAgent() {
        String expectedMessage = String.format("%s: %s", EXPECTED_AGENT_NAME, agentMock);
        agentLogger.agentInfo();
        verify(loggerMock).info(contains(expectedMessage));
    }

    @Test
    public void shouldLogAgentInfo() {
        String expectedInfo = "expectedInfo";
        String expectedMessage = String.format("%s: %s", EXPECTED_AGENT_NAME, expectedInfo);
        agentLogger.info(expectedInfo);
        verify(loggerMock).info(contains(expectedMessage));
    }

}