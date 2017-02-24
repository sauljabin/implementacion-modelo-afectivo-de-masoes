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
import jade.domain.FIPANames;
import ontology.settings.SettingsOntology;
import org.slf4j.LoggerFactory;
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
            agentManagementAssistant.register(
                    createService("GetSetting"),
                    createService("GetAllSettings")
            );
        } catch (Exception e) {
            logger.exception(this, e);
        }

        addBehaviour(new ResponseSettingsBehaviour(this));
    }

    private ServiceDescription createService(String serviceName) {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(serviceName);
        serviceDescription.setType(getLocalName() + "-" + serviceName);
        serviceDescription.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
        serviceDescription.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
        serviceDescription.addOntologies(SettingsOntology.NAME);
        return serviceDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("aid", getAID())
                .toString();
    }

}
