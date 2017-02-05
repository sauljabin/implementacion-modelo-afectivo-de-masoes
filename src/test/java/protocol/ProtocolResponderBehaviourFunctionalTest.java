/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import test.FunctionalTest;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ProtocolResponderBehaviourFunctionalTest extends FunctionalTest {

    private AID agent;

    @Before
    public void setUp() {
        agent = createAgent();
    }

    @After
    public void tearDown() {
        killAgent(agent);
    }

    @Test
    public void shouldResponseInform() {
        addBehaviour(agent, ProtocolResponderInformBehaviour.class);

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(agent);

        sendMessage(request);

        ACLMessage agreeResponse = blockingReceive();
        assertThat(agreeResponse.getPerformative(), is(ACLMessage.AGREE));

        ACLMessage response = blockingReceive();
        assertThat(response.getContent(), is("INFORM CONTENT"));
        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
    }

    @Test
    public void shouldResponseFailure() {
        addBehaviour(agent, ProtocolResponderFailureBehaviour.class);

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(agent);

        sendMessage(request);

        ACLMessage agreeResponse = blockingReceive();
        assertThat(agreeResponse.getPerformative(), is(ACLMessage.AGREE));

        ACLMessage response = blockingReceive();
        assertThat(response.getContent(), is("MESSAGE FAILURE"));
        assertThat(response.getPerformative(), is(ACLMessage.FAILURE));
    }

    @Test
    public void shouldResponseNotUnderstood() {
        addBehaviour(agent, ProtocolResponderNotUnderstoodBehaviour.class);

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(agent);

        sendMessage(request);

        ACLMessage response = blockingReceive();
        assertThat(response.getContent(), is("MESSAGE NOT UNDERSTOOD"));
        assertThat(response.getPerformative(), is(ACLMessage.NOT_UNDERSTOOD));
    }

    @Test
    public void shouldResponseRefuse() {
        addBehaviour(agent, ProtocolResponderRefuseBehaviour.class);

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(agent);

        sendMessage(request);

        ACLMessage response = blockingReceive();
        assertThat(response.getContent(), is("MESSAGE REFUSE"));
        assertThat(response.getPerformative(), is(ACLMessage.REFUSE));
    }

}
