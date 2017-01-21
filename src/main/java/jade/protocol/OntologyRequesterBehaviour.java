/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.ontology.exception.FillOntologyContentException;

public class OntologyRequesterBehaviour extends ProtocolRequesterBehaviour {

    private AID receiver;
    private Ontology ontology;
    private AgentAction agentAction;
    private ContentManager contentManager;

    public OntologyRequesterBehaviour(Agent agent, AID receiver, Ontology ontology, AgentAction agentAction) {
        super(agent, new ACLMessage(ACLMessage.REQUEST));
        this.receiver = receiver;
        this.ontology = ontology;
        this.agentAction = agentAction;
        this.contentManager = new ContentManager();
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage message) {
        contentManager.registerLanguage(new SLCodec());
        contentManager.registerOntology(ontology);

        message.setPerformative(ACLMessage.REQUEST);
        message.setOntology(ontology.getName());
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.addReceiver(receiver);

        try {
            contentManager.fillContent(message, new Action(myAgent.getAID(), agentAction));
        } catch (Exception e) {
            throw new FillOntologyContentException(e.getMessage(), e);
        }

        return message;
    }

}
