/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import ontology.settings.SettingsOntology;
import org.slf4j.LoggerFactory;
import util.ServiceBuilder;
import util.ToStringBuilder;

public class SettingsAgent extends Agent {

    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public SettingsAgent() {
        logger = new AgentLogger(LoggerFactory.getLogger(SettingsAgent.class));
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected void setup() {
        try {
            addBehaviour(new ResponseSettingsBehaviour(this));
            agentManagementAssistant.register(
                    createService(SettingsOntology.ACTION_GET_SETTING),
                    createService(SettingsOntology.ACTION_GET_ALL_SETTINGS)
            );
        } catch (Exception e) {
            logger.exception(this, e);
        }
    }

    private ServiceDescription createService(String serviceName) {
        return new ServiceBuilder()
                .fipaRequest()
                .fipaSL()
                .ontology(SettingsOntology.getInstance())
                .name(serviceName)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("aid", getAID())
                .toString();
    }

}
