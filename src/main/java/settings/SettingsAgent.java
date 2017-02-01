/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import agent.AgentLogger;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import org.slf4j.LoggerFactory;
import settings.ontology.SettingsOntology;
import util.ToStringBuilder;

public class SettingsAgent extends Agent {

    private AgentLogger logger;

    public SettingsAgent() {
        logger = new AgentLogger(LoggerFactory.getLogger(SettingsAgent.class));
    }

    @Override
    protected void setup() {
        try {
            ServiceDescription getSetting = createService("GetSetting");
            ServiceDescription getAllSettings = createService("GetAllSettings");

            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.setName(getAID());
            agentDescription.addServices(getSetting);
            agentDescription.addServices(getAllSettings);

            DFService.register(this, agentDescription);
        } catch (FIPAException e) {
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
        serviceDescription.addOntologies(SettingsOntology.ONTOLOGY_NAME);
        return serviceDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("aid", getAID())
                .toString();
    }

}
