/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.lang.acl.MessageTemplate;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ProtocolRequestResponderBehaviourTest {

    private ProtocolRequestResponderBehaviour spyResponderBehaviour;

    @Before
    public void setUp() {
        spyResponderBehaviour = spy(new ProtocolRequestResponderBehaviour());
    }

    @Test
    public void shouldInvokeReset() {
        MessageTemplate mockMessageTemplate = mock(MessageTemplate.class);
        spyResponderBehaviour.setMessageTemplate(mockMessageTemplate);
        verify(spyResponderBehaviour).reset(mockMessageTemplate);
    }

}