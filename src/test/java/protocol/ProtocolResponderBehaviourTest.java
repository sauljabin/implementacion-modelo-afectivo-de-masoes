/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.JadeLogger;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ProtocolResponderBehaviourTest {

    private static final String EXCEPTION_MESSAGE = "MESSAGE";
    private ProtocolResponderBehaviour responderBehaviourSpy;
    private JadeLogger loggerMock;
    private ProtocolResponderBehaviour responderBehaviour;
    private Agent agentMock;
    private ACLMessage requestMock;
    private ACLMessage responseMock;
    private ACLMessage request;

    @Before
    public void setUp() throws Exception {
        loggerMock = mock(JadeLogger.class);
        agentMock = mock(Agent.class);
        requestMock = mock(ACLMessage.class);
        responseMock = mock(ACLMessage.class);
        responderBehaviour = new ProtocolResponderBehaviour(agentMock, mock(MessageTemplate.class));
        setFieldValue(responderBehaviour, "logger", loggerMock);
        responderBehaviourSpy = spy(responderBehaviour);
        request = new ACLMessage(ACLMessage.REQUEST);
    }

    @Test
    public void shouldInvokeReset() {
        MessageTemplate messageTemplateMock = mock(MessageTemplate.class);
        responderBehaviourSpy.setMessageTemplate(messageTemplateMock);
        verify(responderBehaviourSpy).reset(messageTemplateMock);
    }

    @Test
    public void shouldLogRequestAndResponseWhenPrepareResponse() throws Exception {
        doReturn(responseMock).when(responderBehaviourSpy).prepareAcceptanceResponse(requestMock);
        responderBehaviourSpy.prepareResponse(requestMock);
        verify(loggerMock).messageRequest(agentMock, requestMock);
        verify(loggerMock).messageResponse(agentMock, responseMock);
    }

    @Test
    public void shouldLogRequestAndResponseWhenPrepareResultResponse() throws Exception {
        doReturn(responseMock).when(responderBehaviourSpy).prepareInformResultResponse(requestMock, responseMock);
        responderBehaviourSpy.prepareResultNotification(requestMock, responseMock);
        verify(loggerMock).messageResponse(agentMock, responseMock);
    }

    @Test
    public void shouldPrepareResponseRefuseWhenThrowsRefuseException() throws Exception {
        testResponseFromException(new RefuseException(EXCEPTION_MESSAGE), ACLMessage.REFUSE);
    }

    @Test
    public void shouldPrepareResponseFailureWhenThrowsAnyException() throws Exception {
        testResponseFromException(new RuntimeException(EXCEPTION_MESSAGE), ACLMessage.REFUSE);
    }

    @Test
    public void shouldPrepareResponseNotUnderstoodWhenThrowsNotUnderstoodException() throws Exception {
        testResponseFromException(new NotUnderstoodException(EXCEPTION_MESSAGE), ACLMessage.NOT_UNDERSTOOD);
    }

    @Test
    public void shouldPrepareResultResponseFailureWhenThrowsFailureException() throws Exception {
        testResponseResultFromException(new FailureException(EXCEPTION_MESSAGE), ACLMessage.FAILURE);
    }

    @Test
    public void shouldPrepareResultResponseFailureWhenThrowsAnyException() throws Exception {
        testResponseResultFromException(new RuntimeException(EXCEPTION_MESSAGE), ACLMessage.FAILURE);
    }

    private void testResponseFromException(Exception toBeThrown, int performative) throws Exception {
        doThrow(toBeThrown).when(responderBehaviourSpy).prepareAcceptanceResponse(request);
        ACLMessage response = responderBehaviourSpy.prepareResponse(request);
        assertThat(response.getPerformative(), is(performative));
        assertThat(response.getContent(), is(toBeThrown.getMessage()));
        verify(loggerMock).exception(eq(agentMock), any());
        verify(loggerMock).messageResponse(agentMock, response);

    }

    private void testResponseResultFromException(Exception toBeThrown, int performative) throws Exception {
        ACLMessage response = request.createReply();
        doThrow(toBeThrown).when(responderBehaviourSpy).prepareInformResultResponse(request, response);
        response = responderBehaviourSpy.prepareResultNotification(request, response);
        assertThat(response.getPerformative(), is(performative));
        assertThat(response.getContent(), is(toBeThrown.getMessage()));
        verify(loggerMock).exception(eq(agentMock), any());
        verify(loggerMock).messageResponse(agentMock, response);
    }

}