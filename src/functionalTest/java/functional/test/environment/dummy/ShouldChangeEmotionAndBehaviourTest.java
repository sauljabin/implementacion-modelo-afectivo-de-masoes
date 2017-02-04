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
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import ontology.masoes.AgentStatus;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetAgentStatus;
import ontology.masoes.MasoesOntology;
import ontology.masoes.Stimulus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ShouldChangeEmotionAndBehaviourTest extends FunctionalTest {

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
        testEvaluateStimulus(requestMessage);
        AgentStatus secondEmotionalResponse = testGetStatus(requestMessage);
        assertThat(secondEmotionalResponse.getAgent(), is(firstEmotionalResponse.getAgent()));
        assertThat(secondEmotionalResponse.getEmotionStatus().getActivation(), is(not(firstEmotionalResponse.getEmotionStatus().getActivation())));
        assertThat(secondEmotionalResponse.getEmotionStatus().getSatisfaction(), is(not(firstEmotionalResponse.getEmotionStatus().getSatisfaction())));
    }

    private Done testEvaluateStimulus(ACLMessage requestMessage) throws Exception {
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(new Stimulus());
        Action action = new Action(dummyAgentAID, evaluateStimulus);
        contentManager.fillContent(requestMessage, action);
        sendMessage(requestMessage);

        ACLMessage response = testMessage(requestMessage);

        ContentElement contentElement = contentManager.extractContent(response);
        assertThat(contentElement, is(instanceOf(Done.class)));
        return (Done) contentElement;
    }

    private AgentStatus testGetStatus(ACLMessage requestMessage) throws Exception {
        GetAgentStatus getAgentStatus = new GetAgentStatus();
        Action action = new Action(dummyAgentAID, getAgentStatus);
        contentManager.fillContent(requestMessage, action);
        sendMessage(requestMessage);

        ACLMessage response = testMessage(requestMessage);

        ContentElement contentElement = contentManager.extractContent(response);
        assertThat(contentElement, is(instanceOf(AgentStatus.class)));
        return (AgentStatus) contentElement;
    }

    private ACLMessage testMessage(ACLMessage requestMessage) {
        ACLMessage response = blockingReceive();
        assertThat(response, is(notNullValue()));
        assertThat(response.getPerformative(), is(ACLMessage.AGREE));
        assertThat(response.getConversationId(), is(requestMessage.getConversationId()));

        response = blockingReceive();
        assertThat(response, is(notNullValue()));
        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        assertThat(response.getConversationId(), is(requestMessage.getConversationId()));
        return response;
    }

}
