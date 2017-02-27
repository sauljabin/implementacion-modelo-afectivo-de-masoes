/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import ontology.masoes.MasoesOntology;
import org.slf4j.LoggerFactory;
import settings.SettingsAgent;
import util.ServiceBuilder;

public class NotifierAgent extends Agent {

    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public NotifierAgent() {
        logger = new AgentLogger(LoggerFactory.getLogger(SettingsAgent.class));
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected void setup() {
        try {
            addBehaviour(new NotifierAgentBehaviour(this));

            ServiceDescription serviceDescription = new ServiceBuilder()
                    .fipaRequest()
                    .fipaSL()
                    .ontology(MasoesOntology.getInstance())
                    .name(MasoesOntology.ACTION_NOTIFY_ACTION)
                    .build();

            agentManagementAssistant.register(serviceDescription);
        } catch (Exception e) {
            logger.exception(this, e);
            throw e;
        }
    }

}
