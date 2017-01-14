/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.language.FipaLanguage;
import jade.logger.JadeLogger;
import jade.settings.behaviour.ResponseSettingsBehaviour;
import jade.settings.ontology.SettingsOntology;
import org.slf4j.LoggerFactory;

public class SettingsAgent extends Agent {

    private JadeLogger logger;

    public SettingsAgent() {
        logger = new JadeLogger(LoggerFactory.getLogger(SettingsAgent.class));
    }

    @Override
    protected void setup() {
        getContentManager().registerOntology(SettingsOntology.getInstance());
        getContentManager().registerLanguage(FipaLanguage.getInstance());

        try {
            ServiceDescription getSetting = createService("GetSetting");
            ServiceDescription getAllSettings = createService("GetAllSettings");

            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.setName(getAID());
            agentDescription.addServices(getSetting);
            agentDescription.addServices(getAllSettings);

            DFService.register(this, agentDescription);
        } catch (FIPAException e) {
            logger.agentException(this, e);
        }

        addBehaviour(new ResponseSettingsBehaviour());
    }

    private ServiceDescription createService(String serviceName) {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(serviceName);
        serviceDescription.setType(getLocalName() + "-" + serviceName);
        serviceDescription.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
        serviceDescription.addLanguages(FipaLanguage.LANGUAGE_NAME);
        serviceDescription.addOntologies(SettingsOntology.ONTOLOGY_NAME);
        return serviceDescription;
    }

}
