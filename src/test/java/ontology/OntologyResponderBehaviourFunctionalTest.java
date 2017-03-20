/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import agent.configurable.ConfigurableAgent;
import jade.content.ContentElement;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.ProtocolAssistant;
import test.FunctionalTest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class OntologyResponderBehaviourFunctionalTest extends FunctionalTest {

    private AID agent;
    private OntologyAssistant ontologyAssistant;
    private ProtocolAssistant protocolAssistant;

    @Before
    public void setUp() {
        agent = createAgent(ConfigurableAgent.class, null);
        ontologyAssistant = createOntologyAssistant(JADEManagementOntology.getInstance());
        protocolAssistant = createProtocolAssistant();
    }

    @After
    public void tearDown() {
        killAgent(agent);
    }

    @Test
    public void shouldReceiveDoneFromValidAction() {
        addBehaviour(agent, OntologyResponderValidActionBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);

        ACLMessage requestAction = ontologyAssistant.createRequestAction(agent, action);
        ACLMessage message = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);
        ContentElement contentElement = ontologyAssistant.extractMessageContent(message);

        assertThat(contentElement, is(instanceOf(Done.class)));
    }

    @Test
    public void shouldReceiveNotUnderstoodFromIncorrectAction() {
        addBehaviour(agent, OntologyResponderNotUnderstoodBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);

        ACLMessage requestAction = ontologyAssistant.createRequestAction(agent, action);
        ACLMessage message = protocolAssistant.sendRequest(requestAction, ACLMessage.NOT_UNDERSTOOD);

        assertThat(message.getContent(), is("Unknown ontology JADE-Agent-Management"));
        assertThat(message.getPerformative(), is(ACLMessage.NOT_UNDERSTOOD));
    }

    @Test
    public void shouldReceiveRefuseFromInvalidAction() {
        addBehaviour(agent, OntologyResponderInvalidActionBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);

        ACLMessage requestAction = ontologyAssistant.createRequestAction(agent, action);
        ACLMessage message = protocolAssistant.sendRequest(requestAction, ACLMessage.REFUSE);

        assertThat(message.getContent(), is("Invalid action"));
        assertThat(message.getPerformative(), is(ACLMessage.REFUSE));
    }

    @Test
    public void shouldReceiveFailureFromExceptionInAgent() {
        addBehaviour(agent, OntologyResponderFailureBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);

        ACLMessage requestAction = ontologyAssistant.createRequestAction(agent, action);
        ACLMessage message = protocolAssistant.sendRequest(requestAction, ACLMessage.FAILURE);

        assertThat(message.getContent(), is("MESSAGE FAILURE"));
        assertThat(message.getPerformative(), is(ACLMessage.FAILURE));
    }

}
