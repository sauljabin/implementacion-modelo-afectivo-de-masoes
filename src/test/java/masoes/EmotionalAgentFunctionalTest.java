/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import environment.dummy.DummyEmotionalAgent;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import ontology.masoes.AgentState;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;
import ontology.masoes.Stimulus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.FunctionalTest;
import util.MessageBuilder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class EmotionalAgentFunctionalTest extends FunctionalTest {

    private AID dummyAgentAID;
    private ContentManager contentManager;

    @Before
    public void setUp() {
        dummyAgentAID = createAgent(DummyEmotionalAgent.class, null);
        contentManager = new ContentManager();
        contentManager.registerOntology(MasoesOntology.getInstance());
        contentManager.registerLanguage(new SLCodec());
    }

    @After
    public void tearDown() throws Exception {
        killAgent(dummyAgentAID);
    }

    @Test
    public void shouldChangeAgentEmotion() throws Exception {
        ACLMessage requestMessage = new MessageBuilder().request()
                .fipaSL()
                .fipaRequest()
                .receiver(dummyAgentAID)
                .ontology(MasoesOntology.getInstance())
                .build();
        testEvaluateStimulus(requestMessage, "bye");
        AgentState firstEmotionalResponse = testGetStatus(requestMessage);
        assertThat(firstEmotionalResponse.getEmotionState().getName(), is("depression"));
        testEvaluateStimulus(requestMessage, "hello");
        AgentState secondEmotionalResponse = testGetStatus(requestMessage);
        assertThat(secondEmotionalResponse.getAgent(), is(firstEmotionalResponse.getAgent()));
        assertThat(secondEmotionalResponse.getEmotionState().getName(), is("happiness"));
    }

    @Test
    public void shouldGetRandomValueFromGetStatus() throws Exception {
        ACLMessage requestMessage = new MessageBuilder().request()
                .fipaSL()
                .fipaRequest()
                .receiver(dummyAgentAID)
                .ontology(MasoesOntology.getInstance())
                .build();
        testGetStatus(requestMessage);
    }

    private Done testEvaluateStimulus(ACLMessage requestMessage, String actionName) throws Exception {
        Stimulus stimulus = new Stimulus();
        stimulus.setActor(getAID());
        stimulus.setActionName(actionName);
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);
        Action action = new Action(dummyAgentAID, evaluateStimulus);
        contentManager.fillContent(requestMessage, action);
        sendMessage(requestMessage);

        ACLMessage response = testMessage(requestMessage);

        ContentElement contentElement = contentManager.extractContent(response);
        assertThat(contentElement, is(instanceOf(Done.class)));
        return (Done) contentElement;
    }

    private AgentState testGetStatus(ACLMessage requestMessage) throws Exception {
        GetEmotionalState getEmotionalState = new GetEmotionalState();
        Action action = new Action(dummyAgentAID, getEmotionalState);
        contentManager.fillContent(requestMessage, action);
        sendMessage(requestMessage);

        ACLMessage response = testMessage(requestMessage);

        ContentElement contentElement = contentManager.extractContent(response);
        assertThat(contentElement, is(instanceOf(AgentState.class)));
        return (AgentState) contentElement;
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
