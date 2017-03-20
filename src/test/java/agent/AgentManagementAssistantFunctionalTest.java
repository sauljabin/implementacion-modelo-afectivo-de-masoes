/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import agent.configurable.ConfigurableAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import org.junit.Test;
import test.FunctionalTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AgentManagementAssistantFunctionalTest extends FunctionalTest {

    @Test
    public void shouldCreateAndKillAgentWithAssistant() {
        AID agent = createAgent(ConfigurableAgent.class, null);
        getAgent(agent.getLocalName());
        killAgent(agent);
        try {
            getAgent(agent.getLocalName());
            fail("Agent not killed");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(agent.getName()));
        }
    }

    @Test
    public void shouldRegisterAgentOnDF() {
        String nameService = "nameService";

        AID agentA = createAgent(Agent.class, null);
        registerService(nameService, agentA);

        AID agentB = createAgent(Agent.class, null);
        registerService(nameService, agentB);

        AID agentC = createAgent(Agent.class, null);
        registerService(nameService, agentC);

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(nameService);

        List<AID> results = search(serviceDescription);
        assertThat(results, hasItems(agentA, agentB, agentC));
    }

    @Test
    public void shouldDeRegisterAgentOnDF() {
        String nameService = "nameServiceToDeregister";

        AID agent = createAgent(Agent.class, null);
        registerService(nameService, agent);

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(nameService);

        List<AID> results = search(serviceDescription);
        assertThat(results, hasSize(1));

        deregister(agent);

        results = search(serviceDescription);
        assertThat(results, hasSize(0));
    }

    @Test
    public void shouldFindAllServices() {
        String nameServiceA = "nameServiceA";
        String nameServiceB = "nameServiceB";

        AID agentA = createAgent(Agent.class, null);

        ServiceDescription serviceDescriptionA = new ServiceDescription();
        serviceDescriptionA.setType(nameServiceA);
        serviceDescriptionA.setName(nameServiceA);

        ServiceDescription serviceDescriptionB = new ServiceDescription();
        serviceDescriptionB.setType(nameServiceB);
        serviceDescriptionB.setName(nameServiceB);

        register(agentA, serviceDescriptionA, serviceDescriptionB);

        List<ServiceDescription> services = services(agentA);

        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());

        assertThat(results, hasItems(nameServiceA, nameServiceB));
    }

    @Test
    public void shouldSearchAllAgents() {
        AID agentA = createAgent(ConfigurableAgent.class, null);
        AID agentB = createAgent(ConfigurableAgent.class, null);
        AID agentC = createAgent(ConfigurableAgent.class, null);
        AID agentD = createAgent("dfx", ConfigurableAgent.class, null);
        List<AID> results = agents();
        assertThat(results, hasItems(agentA, agentB, agentC, agentD));
        assertThat(results, not(hasItems(getAMS(), getDF())));
    }

    private void registerService(String nameService, AID agent) {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(nameService);
        serviceDescription.setName(nameService);
        register(agent, serviceDescription);
    }

}
