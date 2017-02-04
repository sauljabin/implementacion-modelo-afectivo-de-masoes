/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OntologyMatchExpression implements MessageTemplate.MatchExpression {

    private String ontologyName;

    public OntologyMatchExpression(String ontologyName) {
        this.ontologyName = ontologyName;
    }

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

        if (message.getOntology() == null || !message.getOntology().equals(ontologyName)) {
            return false;
        }

        return true;
    }

}
