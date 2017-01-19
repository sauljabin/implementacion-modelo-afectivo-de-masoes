/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.protocol.ProtocolRequestResponderBehaviour;
import masoes.core.ontology.MasoesOntology;

public class MasoesResponderBehaviour extends ProtocolRequestResponderBehaviour {

    private MessageTemplate template;

    public MasoesResponderBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void onStart() {
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        template = MessageTemplate.and(template, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        template = MessageTemplate.and(template, MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL));
        template = MessageTemplate.and(template, MessageTemplate.MatchOntology(MasoesOntology.ONTOLOGY_NAME));
        setMessageTemplate(template);
    }

}
