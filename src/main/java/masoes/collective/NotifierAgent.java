/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import masoes.ontology.MasoesOntology;
import util.ServiceBuilder;

public class NotifierAgent extends Agent {

    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public NotifierAgent() {
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected void setup() {
        try {
            addBehaviour(new NotifierBehaviour(this));
            agentManagementAssistant.register(
                    createServiceDescription(MasoesOntology.ACTION_NOTIFY_EVENT),
                    createServiceDescription(MasoesOntology.ACTION_NOTIFY_ACTION),
                    createServiceDescription(MasoesOntology.ACTION_NOTIFY_OBJECT)
            );
        } catch (Exception e) {
            logger.exception(e);
            throw e;
        }
    }

    private ServiceDescription createServiceDescription(String action) {
        return new ServiceBuilder()
                .fipaRequest()
                .fipaSL()
                .ontology(MasoesOntology.getInstance())
                .name(action)
                .build();
    }

}
