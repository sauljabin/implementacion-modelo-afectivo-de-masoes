/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import agent.AgentLogger;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ProtocolRequesterBehaviourTest {

    private ProtocolRequesterBehaviour resquesterBehaviourSpy;
    private AgentLogger loggerMock;
    private ProtocolRequesterBehaviour requesterBehaviour;
    private Agent agentMock;
    private ACLMessage requestMock;
    private ACLMessage responseMock;
    private Vector<ACLMessage> messages;

    @Before
    public void setUp() throws Exception {
        loggerMock = mock(AgentLogger.class);
        agentMock = mock(Agent.class);
        responseMock = mock(ACLMessage.class);
        requestMock = mock(ACLMessage.class);
        requesterBehaviour = new ProtocolRequesterBehaviour(agentMock, requestMock);
        setFieldValue(requesterBehaviour, "logger", loggerMock);
        resquesterBehaviourSpy = spy(requesterBehaviour);
        messages = new Vector<>();
        messages.add(responseMock);
    }

    @Test
    public void shouldInvokeReset() {
        ACLMessage messageMock = mock(ACLMessage.class);
        resquesterBehaviourSpy.setMessage(messageMock);
        verify(resquesterBehaviourSpy).reset(messageMock);
    }

    @Test
    public void shouldLogRequestAndResponseWhenPrepareResponse() {
        doReturn(requestMock).when(resquesterBehaviourSpy).prepareRequestInteraction(requestMock);
        resquesterBehaviourSpy.prepareRequest(requestMock);
        verify(loggerMock).messageRequest(agentMock, requestMock);
    }

    @Test
    public void shouldLogAllResponse() {
        requesterBehaviour.handleAllResponses(messages);
        verify(loggerMock).messageResponse(agentMock, responseMock);
    }

    @Test
    public void shouldLogAllResults() {
        requesterBehaviour.handleAllResultNotifications(messages);
        verify(loggerMock).messageResponse(agentMock, responseMock);
    }

}