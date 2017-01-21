/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ProtocolResponderBehaviourTest {

    private ProtocolResponderBehaviour responderBehaviourSpy;

    @Before
    public void setUp() {
        responderBehaviourSpy = spy(new ProtocolResponderBehaviour(mock(Agent.class), mock(MessageTemplate.class)));
    }

    @Test
    public void shouldInvokeReset() {
        MessageTemplate messageTemplateMock = mock(MessageTemplate.class);
        responderBehaviourSpy.setMessageTemplate(messageTemplateMock);
        verify(responderBehaviourSpy).reset(messageTemplateMock);
    }

}