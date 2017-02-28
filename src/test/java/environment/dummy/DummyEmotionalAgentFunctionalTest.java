/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import ontology.masoes.ActionStimulus;
import ontology.masoes.AgentState;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.FunctionalTest;
import util.MessageBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class DummyEmotionalAgentFunctionalTest extends FunctionalTest {

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
    public void shouldGetAllEmotionalServicesFromDF() {
        List<ServiceDescription> services = services(dummyAgentAID);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(MasoesOntology.ACTION_EVALUATE_STIMULUS, MasoesOntology.ACTION_GET_EMOTIONAL_STATE));
    }

    @Test
    public void shouldChangeAgentEmotion() throws Exception {
        ACLMessage requestMessage = new MessageBuilder().request()
                .fipaSL()
                .fipaRequest()
                .receiver(dummyAgentAID)
                .ontology(MasoesOntology.getInstance())
                .build();

        testEvaluateStimulus(requestMessage, "greeting", getAID(), "compassion", "IMITATIVE");
        testEvaluateStimulus(requestMessage, "smile", getAID(), "admiration", "IMITATIVE");
        testEvaluateStimulus(requestMessage, "run", getAID(), "rejection", "COGNITIVE");
        testEvaluateStimulus(requestMessage, "bye", getAID(), "anger", "REACTIVE");

        testEvaluateStimulus(requestMessage, "eat", dummyAgentAID, "happiness", "IMITATIVE");
        testEvaluateStimulus(requestMessage, "sleep", dummyAgentAID, "joy", "IMITATIVE");
        testEvaluateStimulus(requestMessage, "wake", dummyAgentAID, "sadness", "COGNITIVE");
        testEvaluateStimulus(requestMessage, "pay", dummyAgentAID, "depression", "REACTIVE");
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

    private void testEvaluateStimulus(ACLMessage requestMessage, String actionName, AID aid, String expectedEmotion, String behaviourType) throws Exception {
        ActionStimulus stimulus = new ActionStimulus();
        stimulus.setActor(aid);
        stimulus.setActionName(actionName);
        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);
        Action action = new Action(dummyAgentAID, evaluateStimulus);
        contentManager.fillContent(requestMessage, action);
        sendMessage(requestMessage);

        ACLMessage response = testMessage(requestMessage);

        ContentElement contentElement = contentManager.extractContent(response);
        assertThat(contentElement, is(instanceOf(Done.class)));

        AgentState secondEmotionalResponse = testGetStatus(requestMessage);
        assertThat(secondEmotionalResponse.getEmotionState().getName(), is(expectedEmotion));
        assertThat(secondEmotionalResponse.getBehaviourState().getType(), is(behaviourType.toUpperCase()));
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
