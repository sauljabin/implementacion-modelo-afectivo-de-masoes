/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import agent.configurable.ConfigurableAgent;
import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import masoes.ontology.MasoesOntology;
import masoes.ontology.notifier.NotifyAction;
import masoes.ontology.notifier.NotifyEvent;
import masoes.ontology.notifier.NotifyObject;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import ontology.OntologyAssistant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.ProtocolAssistant;
import test.FunctionalTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class NotifierAgentFunctionalTest extends FunctionalTest {

    private AID notifierAgent;
    private AID configurableAgent;

    @Before
    public void setUp() {
        notifierAgent = createAgent(NotifierAgent.class, null);
        configurableAgent = createAgent(ConfigurableAgent.class, null);

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(MasoesOntology.ACTION_EVALUATE_STIMULUS);
        serviceDescription.setType(MasoesOntology.ACTION_EVALUATE_STIMULUS);
        register(serviceDescription);
    }

    @After
    public void tearDown() {
        killAgent(notifierAgent);
        deregister();
    }

    @Test
    public void shouldGetAllServicesFromDF() {
        List<ServiceDescription> services = services(notifierAgent);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(MasoesOntology.ACTION_NOTIFY_EVENT, MasoesOntology.ACTION_NOTIFY_ACTION, MasoesOntology.ACTION_NOTIFY_OBJECT));
    }

    @Test
    public void shouldNotifyAction() {
        String expectedActionName = "expectedActionName";
        NotifyAction notifyAction = new NotifyAction(new ActionStimulus(configurableAgent, expectedActionName));

        EvaluateStimulus evaluateStimulus = testSendNotificationEvaluateStimulus(notifyAction);

        ActionStimulus actionStimulus = (ActionStimulus) evaluateStimulus.getStimulus();
        assertThat(actionStimulus.getActionName(), is(expectedActionName));
        assertThat(actionStimulus.getActor(), is(configurableAgent));
    }

    @Test
    public void shouldNotifyEvent() {
        String expectedEventName = "expectedEventName";
        NotifyEvent notifyAction = new NotifyEvent(new EventStimulus(configurableAgent, expectedEventName));

        EvaluateStimulus evaluateStimulus = testSendNotificationEvaluateStimulus(notifyAction);

        EventStimulus eventStimulus = (EventStimulus) evaluateStimulus.getStimulus();
        assertThat(eventStimulus.getEventName(), is(expectedEventName));
        assertThat(eventStimulus.getAffected(), is(configurableAgent));
    }

    @Test
    public void shouldNotifyObject() {
        String expectedObjectName = "expectedObjectName";
        NotifyObject notifyObject = new NotifyObject(new ObjectStimulus(configurableAgent, expectedObjectName));

        EvaluateStimulus evaluateStimulus = testSendNotificationEvaluateStimulus(notifyObject);

        ObjectStimulus objectStimulus = (ObjectStimulus) evaluateStimulus.getStimulus();
        assertThat(objectStimulus.getObjectName(), is(expectedObjectName));
        assertThat(objectStimulus.getCreator(), is(configurableAgent));
    }

    private EvaluateStimulus testSendNotificationEvaluateStimulus(AgentAction agentAction) {
        OntologyAssistant ontologyAssistant = createOntologyAssistant(MasoesOntology.getInstance());
        ProtocolAssistant protocolAssistant = createProtocolAssistant();

        ACLMessage requestAction = ontologyAssistant.createRequestAction(notifierAgent, agentAction);
        ACLMessage response = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);

        ContentElement contentElement = ontologyAssistant.extractMessageContent(response);
        assertThat(contentElement, is(instanceOf(Done.class)));

        ACLMessage stimulus = blockingReceive();

        Action action = (Action) ontologyAssistant.extractMessageContent(stimulus);

        ContentElement contentElementStimulus = (ContentElement) action.getAction();
        assertThat(contentElementStimulus, is(instanceOf(EvaluateStimulus.class)));
        return (EvaluateStimulus) contentElementStimulus;
    }


}