/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.agent;

import application.logger.ApplicationLogger;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.settings.behaviour.ReplaySettingsBehaviour;
import org.slf4j.LoggerFactory;

public class SettingsAgent extends Agent {

    private static final String SERVICE_NAME = "get-setting";
    private ApplicationLogger logger;

    public SettingsAgent() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(SettingsAgent.class));
    }

    @Override
    protected void setup() {
        try {
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setName(SERVICE_NAME);
            serviceDescription.setType(getLocalName() + "-" + SERVICE_NAME);
            serviceDescription.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);

            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.setName(getAID());
            agentDescription.addServices(serviceDescription);

            DFService.register(this, agentDescription);
        } catch (FIPAException e) {
            logger.agentException(this, e);
        }

        addBehaviour(new ReplaySettingsBehaviour());
    }

}
