/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.environment.dummy;

import environment.dummy.DummyEmotionalAgent;
import functional.test.FunctionalTest;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import ontology.masoes.AgentStatus;
import ontology.masoes.GetAgentStatus;
import ontology.masoes.MasoesOntology;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ShouldChangeEmotionAndBehaviourTest extends FunctionalTest {

    private static final int MILLIS = 60000;
    private AID dummyAgentAID;
    private ContentManager contentManager;

    @Override
    public void setUp() {
        dummyAgentAID = createAgent(DummyEmotionalAgent.class);
        contentManager = new ContentManager();
        contentManager.registerOntology(new MasoesOntology());
        contentManager.registerLanguage(new SLCodec());
    }

    @Override
    public void performTest() throws Exception {
        ACLMessage requestMessage = createRequestMessage(dummyAgentAID, MasoesOntology.ONTOLOGY_NAME);
        AgentStatus firstEmotionalResponse = testGetStatus(requestMessage);
        AgentStatus secondEmotionalResponse = testGetStatus(requestMessage);
    }

    private AgentStatus testGetStatus(ACLMessage requestMessage) throws Exception {
        GetAgentStatus getAgentStatus = new GetAgentStatus();
        Action action = new Action(dummyAgentAID, getAgentStatus);
        contentManager.fillContent(requestMessage, action);
        sendMessage(requestMessage);

        ACLMessage response = blockingReceive(MILLIS);
        assertThat(response, is(notNullValue()));
        assertThat(response.getPerformative(), is(ACLMessage.AGREE));
        assertThat(response.getConversationId(), is(requestMessage.getConversationId()));

        response = blockingReceive(MILLIS);
        assertThat(response, is(notNullValue()));
        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        assertThat(response.getConversationId(), is(requestMessage.getConversationId()));

        ContentElement contentElement = contentManager.extractContent(response);
        assertThat(contentElement, is(instanceOf(AgentStatus.class)));
        return (AgentStatus) contentElement;
    }

}
