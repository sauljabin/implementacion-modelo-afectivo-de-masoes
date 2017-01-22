/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logger.jade.JadeLogger;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ProtocolResponderBehaviourTest {

    private ProtocolResponderBehaviour responderBehaviourSpy;
    private JadeLogger loggerMock;
    private ProtocolResponderBehaviour responderBehaviour;
    private Agent agentMock;
    private ACLMessage requestMock;
    private ACLMessage responseMock;

    @Before
    public void setUp() throws Exception {
        loggerMock = mock(JadeLogger.class);
        agentMock = mock(Agent.class);
        requestMock = mock(ACLMessage.class);
        responseMock = mock(ACLMessage.class);
        responderBehaviour = new ProtocolResponderBehaviour(agentMock, mock(MessageTemplate.class));
        setFieldValue(responderBehaviour, "logger", loggerMock);
        responderBehaviourSpy = spy(responderBehaviour);
    }

    @Test
    public void shouldInvokeReset() {
        MessageTemplate messageTemplateMock = mock(MessageTemplate.class);
        responderBehaviourSpy.setMessageTemplate(messageTemplateMock);
        verify(responderBehaviourSpy).reset(messageTemplateMock);
    }

    @Test
    public void shouldLogRequestAndResponseWhenPrepareResponse() {
        doReturn(responseMock).when(responderBehaviourSpy).prepareAcceptanceResponse(requestMock);
        responderBehaviourSpy.prepareResponse(requestMock);
        verify(loggerMock).messageRequest(agentMock, requestMock);
        verify(loggerMock).messageResponse(agentMock, responseMock);
    }

    @Test
    public void shouldLogRequestAndResponseWhenPrepareResultResponse() {
        doReturn(responseMock).when(responderBehaviourSpy).prepareInformResultResponse(requestMock, responseMock);
        responderBehaviourSpy.prepareResultNotification(requestMock, responseMock);
        verify(loggerMock).messageResponse(agentMock, responseMock);
    }

}