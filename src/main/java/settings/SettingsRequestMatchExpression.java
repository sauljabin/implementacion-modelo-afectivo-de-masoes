/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.settings.SettingsOntology;

public class SettingsRequestMatchExpression implements MessageTemplate.MatchExpression {

    @Override
    public boolean match(ACLMessage message) {
        if (message.getPerformative() != ACLMessage.REQUEST) {
            return false;
        }

        if (!message.getProtocol().equals(FIPANames.InteractionProtocol.FIPA_REQUEST)) {
            return false;
        }

        if (!message.getLanguage().equals(FIPANames.ContentLanguage.FIPA_SL)) {
            return false;
        }

        if (!message.getOntology().equals(SettingsOntology.ONTOLOGY_NAME)) {
            return false;
        }

        return true;
    }

}
