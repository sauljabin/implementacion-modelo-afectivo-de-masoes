/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import test.PowerMockitoTest;
import util.StopWatch;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ProtocolAssistantTest extends PowerMockitoTest {

    private static final String AGENT_NAME = "agentName";
    private static final String RECEIVER_NAME = "receiverName";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ProtocolAssistant protocolAssistant;
    private Agent agentMock;
    private AID aid;
    private AID receiver;
    private StopWatch stopWatchMock;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        stopWatchMock = mock(StopWatch.class);
        aid = new AID(AGENT_NAME, AID.ISGUID);
        receiver = new AID(RECEIVER_NAME, AID.ISGUID);
        doReturn(aid).when(agentMock).getAID();
        protocolAssistant = new ProtocolAssistant(agentMock);
        setFieldValue(protocolAssistant, "stopWatch", stopWatchMock);
    }

    @Test
    public void shouldCreateRequestMessage() {
        String expectedContent = "content";
        ACLMessage actualMessage = protocolAssistant.createRequestMessage(receiver, expectedContent);
        assertThat(actualMessage.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(actualMessage.getSender(), is(aid));
        assertThat(actualMessage.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actualMessage.getReplyWith().length(), is(20));
        assertThat(actualMessage.getConversationId().length(), is(20));
        assertThat(actualMessage.getContent(), is(expectedContent));
        assertThat(actualMessage.getAllReceiver().next(), is(receiver));
    }

    @Test
    public void shouldVerifyTimeoutAndSendMessageControl() throws Exception {
        long timeout = 5000L;
        long delayInAgree = 2000L;
        ACLMessage responseMock = mock(ACLMessage.class);

        doReturn(ACLMessage.AGREE).doReturn(ACLMessage.INFORM).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        doReturn(delayInAgree).when(stopWatchMock).getTime();

        ACLMessage expectedMessage = new ACLMessage(ACLMessage.REQUEST);
        protocolAssistant.sendRequest(expectedMessage);

        InOrder inOrder = inOrder(stopWatchMock, agentMock);
        inOrder.verify(agentMock).send(expectedMessage);
        inOrder.verify(stopWatchMock).reset();
        inOrder.verify(stopWatchMock).start();
        inOrder.verify(agentMock).blockingReceive(any(MessageTemplate.class), eq(timeout));
        inOrder.verify(stopWatchMock).stop();
        inOrder.verify(stopWatchMock).getTime();
        inOrder.verify(agentMock).blockingReceive(any(MessageTemplate.class), eq(timeout - delayInAgree));
    }

    @Test
    public void shouldWaitForMessageAfterAgreeInSendActionAndWaitMessage() throws Exception {
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.AGREE)
                .doReturn(ACLMessage.INFORM)
                .when(responseMock)
                .getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        protocolAssistant.sendRequest(new ACLMessage(ACLMessage.REQUEST));
        verify(agentMock, times(2)).blockingReceive(any(MessageTemplate.class), anyLong());
    }

    @Test
    public void shouldThrowTimeoutExceptionInSendRequestWhenResponseIsNull() throws Exception {
        String expectedMessage = "The agent did not respond";
        doReturn(null).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expect(TimeoutRequestException.class);
        expectedException.expectMessage(expectedMessage);
        protocolAssistant.sendRequest(new ACLMessage(ACLMessage.REQUEST));
    }

    @Test
    public void shouldThrowTimeoutExceptionInSendRequestWhenWaitSecondAndResponseIsNull() throws Exception {
        String expectedMessage = "The agent did not respond";
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.AGREE).when(responseMock).getPerformative();
        doReturn(responseMock)
                .doReturn(null)
                .when(agentMock)
                .blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expect(TimeoutRequestException.class);
        expectedException.expectMessage(expectedMessage);
        protocolAssistant.sendRequest(new ACLMessage(ACLMessage.REQUEST));
    }

    @Test
    public void shouldThrowInvalidResponseExceptionExceptionInSendRequestWhenExpectPerformative() throws Exception {
        ACLMessage responseMock = mock(ACLMessage.class);
        doReturn(ACLMessage.REQUEST).when(responseMock).getPerformative();
        doReturn(responseMock).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        String expectedMessage = String.format("Expected performative: %s and was: %s", ACLMessage.getPerformative(ACLMessage.NOT_UNDERSTOOD), ACLMessage.getPerformative(ACLMessage.REQUEST));
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage(expectedMessage);
        protocolAssistant.sendRequest(new ACLMessage(ACLMessage.REQUEST), ACLMessage.NOT_UNDERSTOOD);
    }

}