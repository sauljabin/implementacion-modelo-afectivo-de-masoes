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
import test.PowerMockitoTest;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

public class SimpleBehaviourTest extends PowerMockitoTest {

    private SimpleBehaviour simpleBehaviour;
    private Agent agentMock;
    private SimpleBehaviour simpleBehaviourSpy;

    @Before
    public void setUp() {
        agentMock = mock(Agent.class);
        simpleBehaviour = new SimpleBehaviour();
        simpleBehaviour.setAgent(agentMock);
        simpleBehaviourSpy = spy(simpleBehaviour);
    }

    @Test
    public void shouldBlockWaitingMessage() {
        simpleBehaviourSpy.action();
        verify(simpleBehaviourSpy).block();
    }

    @Test
    public void shouldSendResponseWhenReceiveAnyMessage() {
        ACLMessage messageMock = mock(ACLMessage.class);
        ACLMessage requestMock = mock(ACLMessage.class);
        doReturn(messageMock).when(agentMock).receive();
        doReturn(requestMock).when(messageMock).createReply();
        simpleBehaviour.action();
        verify(requestMock).setPerformative(ACLMessage.CONFIRM);
        verify(agentMock).send(requestMock);
    }

}