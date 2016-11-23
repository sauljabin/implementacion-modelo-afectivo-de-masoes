/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.setting;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import masoes.app.logger.ApplicationLogger;
import org.slf4j.LoggerFactory;

public class SettingsAgent extends Agent {

    private ApplicationLogger logger;

    public SettingsAgent() {
        this(new ApplicationLogger(LoggerFactory.getLogger(SettingsAgent.class)));
    }

    public SettingsAgent(ApplicationLogger logger) {
        this.logger = logger;
    }

    @Override
    protected void setup() {
        try {
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setName("get-setting");
            serviceDescription.setType(getLocalName() + "-get-setting");
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
