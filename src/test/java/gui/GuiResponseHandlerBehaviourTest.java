/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class GuiResponseHandlerBehaviourTest {

    private RequesterGuiAgent requesterGuiAgentMock;
    private RequesterGui requesterGuiMock;
    private GuiResponseHandlerBehaviour responseHandlerBehaviour;
    private GuiResponseHandlerBehaviour responseHandlerBehaviourSpy;

    @Before
    public void setUp() {
        requesterGuiAgentMock = mock(RequesterGuiAgent.class);
        requesterGuiMock = mock(RequesterGui.class);
        responseHandlerBehaviourSpy = spy(new GuiResponseHandlerBehaviour(requesterGuiAgentMock, requesterGuiMock));
    }

    @Test
    public void shouldBlockWaitingMessage() {
        responseHandlerBehaviourSpy.action();
        verify(responseHandlerBehaviourSpy).block();
    }

    @Test
    public void shouldSendResponseWhenReceiveAnyMessage() throws Exception {
        ACLMessage messageMock = mock(ACLMessage.class);
        doReturn(messageMock).when(requesterGuiAgentMock).receive();
        responseHandlerBehaviourSpy.action();
        verify(requesterGuiMock).logMessage(messageMock);
    }

}