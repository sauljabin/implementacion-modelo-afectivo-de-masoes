/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import test.FunctionalTest;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ConfigurableAgentFunctionalTest extends FunctionalTest {

    private AID configurableAid;

    @Before
    public void setUp() {
        configurableAid = createAgent();
    }

    @Test
    public void shouldAddAndRemoveBehaviour() {
        String behaviour = addBehaviour(configurableAid, SimpleBehaviour.class);

        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(configurableAid);

        sendMessage(message);

        ACLMessage response = blockingReceive();
        assertThat(response.getPerformative(), is(ACLMessage.CONFIRM));

        removeBehaviour(configurableAid, behaviour);

        try {
            sendMessage(message);
            blockingReceive(1000);
            fail("Expected TimeoutException");
        } catch (TimeoutException e) {
        }
    }

}
