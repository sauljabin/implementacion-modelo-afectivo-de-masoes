/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

public class DummyBehaviourTest extends PowerMockitoTest {

    private DummyBehaviour dummyBehaviour;
    private Agent agentMock;
    private DummyBehaviour dummyBehaviourSpy;

    @Before
    public void setUp() {
        agentMock = mock(Agent.class);
        dummyBehaviour = new DummyBehaviour();
        dummyBehaviour.setAgent(agentMock);
        dummyBehaviourSpy = spy(dummyBehaviour);
    }

    @Test
    public void shouldBlockWaitingMessage() {
        dummyBehaviourSpy.action();
        verify(dummyBehaviourSpy).block();
    }

    @Test
    public void shouldSendResponseWhenReceiveAnyMessage() {
        ACLMessage messageMock = mock(ACLMessage.class);
        ACLMessage requestMock = mock(ACLMessage.class);
        doReturn(messageMock).when(agentMock).receive();
        doReturn(requestMock).when(messageMock).createReply();
        dummyBehaviour.action();
        verify(requestMock).setPerformative(ACLMessage.CONFIRM);
        verify(agentMock).send(requestMock);
    }

}