/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class ProtocolAssistantTest {

    private static final String AGENT_NAME = "agentName";
    private static final String RECEIVER_NAME = "receiverName";
    private ProtocolAssistant protocolAssistant;
    private Agent agentMock;
    private AID aid;
    private AID receiver;

    @Before
    public void setUp() {
        agentMock = mock(Agent.class);
        aid = new AID(AGENT_NAME, AID.ISGUID);
        receiver = new AID(RECEIVER_NAME, AID.ISGUID);
        doReturn(aid).when(agentMock).getAID();
        protocolAssistant = new ProtocolAssistant(agentMock);
    }

    @Test
    public void shouldCreateRequestMessage() {
        String expectedContent = "content";
        ACLMessage actualMessage = protocolAssistant.createRequestMessage(receiver, expectedContent);
        assertThat(actualMessage.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(actualMessage.getSender(), is(aid));
        assertThat(actualMessage.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actualMessage.getReplyWith().length(), is(20));
        assertThat(actualMessage.getConversationId().length(), is(20));
        assertThat(actualMessage.getContent(), is(expectedContent));
        assertThat(actualMessage.getAllReceiver().next(), is(receiver));
    }

    // TODO: TERMINAR PRUEBAS

}