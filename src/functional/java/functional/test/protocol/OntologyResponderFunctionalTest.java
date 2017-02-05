/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol;

import functional.test.FunctionalTest;
import jade.content.ContentElement;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class OntologyResponderFunctionalTest extends FunctionalTest {

    private AID agent;

    @Before
    public void setUp() {
        agent = createAgent();
    }

    @After
    public void tearDown() {
        killAgent(agent);
    }

    @Test
    public void shouldReceiveDoneFromValidAction() throws Exception {
        addBehaviour(agent, OntologyResponderValidActionBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);
        ContentElement contentElement = sendActionAndWaitContent(agent, action, JADEManagementOntology.NAME);
        assertThat(contentElement, is(instanceOf(Done.class)));
    }

    @Test
    public void shouldReceiveNotUnderstoodFromIncorrectAction() throws Exception {
        addBehaviour(agent, OntologyResponderNotUnderstoodBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);
        ACLMessage message = sendActionAndWaitMessage(agent, action, JADEManagementOntology.NAME);
        assertThat(message.getContent(), is("Unknown ontology JADE-Agent-Management"));
        assertThat(message.getPerformative(), is(ACLMessage.NOT_UNDERSTOOD));
    }

    @Test
    public void shouldReceiveRefuseFromInvalidAction() throws Exception {
        addBehaviour(agent, OntologyResponderInvalidActionBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);
        ACLMessage message = sendActionAndWaitMessage(agent, action, JADEManagementOntology.NAME);
        assertThat(message.getContent(), is("Invalid action"));
        assertThat(message.getPerformative(), is(ACLMessage.REFUSE));
    }

    @Test
    public void shouldReceiveFailureFromExceptionInAgent() throws Exception {
        addBehaviour(agent, OntologyResponderFailureBehaviour.class);
        KillAgent action = new KillAgent();
        action.setAgent(agent);
        ACLMessage message = sendActionAndWaitMessage(agent, action, JADEManagementOntology.NAME);
        assertThat(message.getContent(), is("MESSAGE FAILURE"));
        assertThat(message.getPerformative(), is(ACLMessage.FAILURE));
    }

}
