/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.content.onto.Ontology;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OntologyMatchExpression implements MessageTemplate.MatchExpression {

    private Ontology ontology;

    public OntologyMatchExpression(Ontology ontology) {
        this.ontology = ontology;
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

        if (message.getOntology() == null || !message.getOntology().equals(ontology.getName())) {
            return false;
        }

        return true;
    }

}
