/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import masoes.MasoesSettings;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import masoes.ontology.stimulus.Stimulus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.FunctionalTest;
import util.MessageBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DummyEmotionalAgentFunctionalTest extends FunctionalTest {

    private AID dummyAgentAID;
    private ContentManager contentManager;

    @Before
    public void setUp() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_ACTIVATION_PARAMETER, "0.1");
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_SATISFACTION_PARAMETER, "0.1");
        contentManager = new ContentManager();
        contentManager.registerOntology(MasoesOntology.getInstance());
        contentManager.registerLanguage(new SLCodec());
    }

    @After
    public void tearDown() {
        killAgent(dummyAgentAID);
    }

    @Test
    public void shouldGetAllEmotionalServicesFromDF() {
        dummyAgentAID = createAgent(DummyEmotionalAgent.class, null);
        List<ServiceDescription> services = services(dummyAgentAID);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(MasoesOntology.ACTION_EVALUATE_STIMULUS, MasoesOntology.ACTION_GET_EMOTIONAL_STATE));
    }

    @Test
    public void shouldChangeAgentEmotion() throws Exception {
        testEvaluateActionStimulus("greeting", getAID(), "compassion", "IMITATIVE", Arrays.asList("--activation=-0.45", "--satisfaction=0.45"));
        testEvaluateObjectStimulus("smile", getAID(), "admiration", "IMITATIVE", Arrays.asList("--activation=-0.55", "--satisfaction=0.55"));
        testEvaluateEventStimulus("bye", getAID(), "joy", "IMITATIVE", Arrays.asList("--activation=0.55", "--satisfaction=0.55"));
    }

    @Test
    public void shouldGetRandomValueFromGetStatus() throws Exception {
        dummyAgentAID = createAgent(DummyEmotionalAgent.class, null);
        ACLMessage requestMessage = new MessageBuilder().request()
                .fipaSL()
                .fipaRequest()
                .receiver(dummyAgentAID)
                .ontology(MasoesOntology.getInstance())
                .build();
        testGetStatus(requestMessage);
    }

    private void testEvaluateActionStimulus(String actionName, AID aid, String expectedEmotion, String behaviourType, List<String> arguments) throws Exception {
        ActionStimulus stimulus = new ActionStimulus();
        stimulus.setActor(aid);
        stimulus.setActionName(actionName);
        testEvaluateStimulus(expectedEmotion, behaviourType, stimulus, arguments);
    }

    private void testEvaluateObjectStimulus(String actionName, AID aid, String expectedEmotion, String behaviourType, List<String> arguments) throws Exception {
        ObjectStimulus stimulus = new ObjectStimulus();
        stimulus.setCreator(aid);
        stimulus.setObjectName(actionName);
        testEvaluateStimulus(expectedEmotion, behaviourType, stimulus, arguments);
    }

    private void testEvaluateEventStimulus(String actionName, AID aid, String expectedEmotion, String behaviourType, List<String> arguments) throws Exception {
        EventStimulus stimulus = new EventStimulus();
        stimulus.setAffected(aid);
        stimulus.setEventName(actionName);
        testEvaluateStimulus(expectedEmotion, behaviourType, stimulus, arguments);
    }

    private void testEvaluateStimulus(String expectedEmotion, String behaviourType, Stimulus stimulus, List<String> arguments) throws Exception {
        dummyAgentAID = createAgent(DummyEmotionalAgent.class, arguments);

        ACLMessage requestMessage = new MessageBuilder().request()
                .fipaSL()
                .fipaRequest()
                .receiver(dummyAgentAID)
                .ontology(MasoesOntology.getInstance())
                .build();

        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);

        Action action = new Action(dummyAgentAID, evaluateStimulus);
        contentManager.fillContent(requestMessage, action);
        sendMessage(requestMessage);

        ACLMessage response = testMessage(requestMessage);

        ContentElement contentElement = contentManager.extractContent(response);
        assertThat(contentElement, is(instanceOf(AgentState.class)));

        AgentState firstEmotionalResponse = (AgentState) contentElement;

        AgentState secondEmotionalResponse = testGetStatus(requestMessage);
        assertThat(secondEmotionalResponse.getEmotionState().getName(), is(expectedEmotion));
        assertThat(secondEmotionalResponse.getBehaviourState().getType(), is(behaviourType.toUpperCase()));

        assertReflectionEquals(firstEmotionalResponse, secondEmotionalResponse);
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
