/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import agent.configurable.ConfigurableAgent;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import ontology.OntologyAssistant;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.MasoesOntology;
import masoes.ontology.notifier.NotifyAction;
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
    }

    @After
    public void tearDown() {
        killAgent(notifierAgent);
    }

    @Test
    public void shouldGetAllServicesFromDF() {
        List<ServiceDescription> services = services(notifierAgent);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(MasoesOntology.ACTION_NOTIFY_ACTION));
    }

    @Test
    public void shouldNotifyAction() {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(MasoesOntology.ACTION_EVALUATE_STIMULUS);
        serviceDescription.setType(MasoesOntology.ACTION_EVALUATE_STIMULUS);
        register(serviceDescription);

        OntologyAssistant ontologyAssistant = createOntologyAssistant(MasoesOntology.getInstance());
        ProtocolAssistant protocolAssistant = createProtocolAssistant();

        String expectedActionName = "expectedActionName";
        NotifyAction notifyAction = new NotifyAction(new ActionStimulus(configurableAgent, expectedActionName));

        ACLMessage requestAction = ontologyAssistant.createRequestAction(notifierAgent, notifyAction);
        ACLMessage response = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);

        ContentElement contentElement = ontologyAssistant.extractMessageContent(response);
        assertThat(contentElement, is(instanceOf(Done.class)));

        ACLMessage stimulus = blockingReceive();
        deregister();

        Action action = (Action) ontologyAssistant.extractMessageContent(stimulus);

        ContentElement contentElementStimulus = (ContentElement) action.getAction();
        assertThat(contentElementStimulus, is(instanceOf(EvaluateStimulus.class)));
        EvaluateStimulus evaluateStimulus = (EvaluateStimulus) contentElementStimulus;
        ActionStimulus actionStimulus = (ActionStimulus) evaluateStimulus.getStimulus();
        assertThat(actionStimulus.getActionName(), is(expectedActionName));
        assertThat(actionStimulus.getActor(), is(configurableAgent));
    }

}