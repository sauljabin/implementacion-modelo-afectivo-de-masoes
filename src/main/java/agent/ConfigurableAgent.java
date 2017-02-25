/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import ontology.configurable.ConfigurableOntology;
import org.slf4j.LoggerFactory;
import settings.SettingsAgent;
import util.ServiceBuilder;

public class ConfigurableAgent extends Agent {

    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public ConfigurableAgent() {
        logger = new AgentLogger(LoggerFactory.getLogger(SettingsAgent.class));
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
            logger.exception(this, e);
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
