/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent.configurable;

import agent.configurable.ontology.ConfigurableOntology;
import environment.dummy.DummyBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.TimeoutRequestException;
import test.FunctionalTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ConfigurableAgentFunctionalTest extends FunctionalTest {

    private AID configurableAid;

    @Before
    public void setUp() {
        configurableAid = createAgent(ConfigurableAgent.class, null);
    }

    @After
    public void tearDown() {
        killAgent(configurableAid);
    }

    @Test
    public void shouldGetAllServicesFromDF() {
        List<ServiceDescription> services = services(configurableAid);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(ConfigurableOntology.ACTION_ADD_BEHAVIOUR, ConfigurableOntology.ACTION_REMOVE_BEHAVIOUR));
    }

    @Test
    public void shouldAddAndRemoveBehaviour() {
        String behaviour = addBehaviour(configurableAid, DummyBehaviour.class);

        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(configurableAid);

        sendMessage(message);

        ACLMessage response = blockingReceive();
        assertThat(response.getPerformative(), is(ACLMessage.CONFIRM));

        removeBehaviour(configurableAid, behaviour);

        try {
            sendMessage(message);
            blockingReceive(1000);
            fail("Expected TimeoutRequestException");
        } catch (TimeoutRequestException e) {
        }
    }

}
