/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ProtocolRequesterBehaviourTest {

    private ProtocolRequesterBehaviour responderBehaviourSpy;

    @Before
    public void setUp() {
        responderBehaviourSpy = spy(new ProtocolRequesterBehaviour(mock(Agent.class), mock(ACLMessage.class)));
    }

    @Test
    public void shouldInvokeReset() {
        ACLMessage messageMock = mock(ACLMessage.class);
        responderBehaviourSpy.setMessage(messageMock);
        verify(responderBehaviourSpy).reset(messageMock);
    }

}