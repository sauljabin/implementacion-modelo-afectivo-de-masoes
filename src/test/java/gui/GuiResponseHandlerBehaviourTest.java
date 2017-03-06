/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

public class GuiResponseHandlerBehaviourTest extends PowerMockitoTest {

    private RequesterGuiAgent requesterGuiAgentMock;
    private RequesterGui requesterGuiMock;
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
    public void shouldSendResponseWhenReceiveAnyMessage() {
        ACLMessage messageMock = mock(ACLMessage.class);
        doReturn(messageMock).when(requesterGuiAgentMock).receive();
        responseHandlerBehaviourSpy.action();
        verify(requesterGuiMock).logMessage(messageMock);
    }

}