/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import agent.configurable.ConfigurableAgent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.FunctionalTest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ProtocolAssistantFunctionalTest extends FunctionalTest {

    private AID agent;

    @Before
    public void setUp() {
        agent = createAgent(ConfigurableAgent.class, null);
    }

    @After
    public void tearDown() {
        killAgent(agent);
    }

    @Test
    public void shouldResponseFailure() {
        addBehaviour(agent, ProtocolResponderInformBehaviour.class);
        ProtocolAssistant protocolAssistant = createProtocolAssistant();
        ACLMessage request = protocolAssistant.createRequestMessage(agent, "");

        ACLMessage response = protocolAssistant.sendRequest(request);

        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        assertThat(response.getContent(), is("INFORM CONTENT"));
        assertThat(request.getReplyWith(), is(response.getInReplyTo()));
    }

}
