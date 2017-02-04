/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.configurable.ConfigurableOntology;

public class ConfiguringAgentMatchExpression implements MessageTemplate.MatchExpression {

    @Override
    public boolean match(ACLMessage message) {
        if (message.getPerformative() != ACLMessage.REQUEST) {
            return false;
        }

        if (message.getProtocol() == null || !message.getProtocol().equals(FIPANames.InteractionProtocol.FIPA_REQUEST)) {
            return false;
        }

        if (message.getLanguage() == null || !message.getLanguage().equals(FIPANames.ContentLanguage.FIPA_SL)) {
            return false;
        }

        if (message.getOntology() == null || !message.getOntology().equals(ConfigurableOntology.ONTOLOGY_NAME)) {
            return false;
        }

        return true;
    }

}
