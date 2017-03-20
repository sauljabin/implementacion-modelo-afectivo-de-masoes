/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent.configurable;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import agent.configurable.ontology.ConfigurableOntology;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import util.ServiceBuilder;

public class ConfigurableAgent extends Agent {

    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public ConfigurableAgent() {
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected void setup() {
        try {
            addBehaviour(new ConfiguringAgentBehaviour(this));
            agentManagementAssistant.register(
                    createService(ConfigurableOntology.ACTION_ADD_BEHAVIOUR),
                    createService(ConfigurableOntology.ACTION_REMOVE_BEHAVIOUR)
            );
        } catch (Exception e) {
            logger.exception(e);
            throw e;
        }
    }

    private ServiceDescription createService(String serviceName) {
        return new ServiceBuilder()
                .fipaRequest()
                .fipaSL()
                .ontology(ConfigurableOntology.getInstance())
                .name(serviceName)
                .build();
    }

}
