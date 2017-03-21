/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import masoes.ontology.MasoesOntology;
import util.ServiceBuilder;

public class ColectiveKnowledgeBaseAgent extends Agent {

    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public ColectiveKnowledgeBaseAgent() {
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected void setup() {
        try {
            addBehaviour(new ColectiveKnowledgeBaseBehaviour(this));

            agentManagementAssistant.register(
                    createService(MasoesOntology.ACTION_CREATE_OBJECT),
                    createService(MasoesOntology.ACTION_GET_OBJECT),
                    createService(MasoesOntology.ACTION_UPDATE_OBJECT),
                    createService(MasoesOntology.ACTION_DELETE_OBJECT)
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
                .ontology(MasoesOntology.getInstance())
                .name(serviceName)
                .build();
    }

}
