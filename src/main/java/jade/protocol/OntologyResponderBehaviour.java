/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OntologyResponderBehaviour extends ProtocolResponderBehaviour {

    private final MessageTemplate messageTemplate;
    private ContentManager contentManager;
    private Ontology ontology;

    public OntologyResponderBehaviour(Agent agent, MessageTemplate messageTemplate, Ontology ontology) {
        super(agent, messageTemplate);
        this.messageTemplate = messageTemplate;
        this.ontology = ontology;
        contentManager = new ContentManager();
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public Ontology getOntology() {
        return ontology;
    }

    @Override
    protected final ACLMessage prepareAcceptanceResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        contentManager.registerLanguage(new SLCodec());
        contentManager.registerOntology(ontology);
        ACLMessage response = request.createReply();
        Action action;

        try {
            action = (Action) contentManager.extractContent(request);
        } catch (Exception e) {
            throw new NotUnderstoodException(e.getMessage());
        }

        if (isValidAction(action)) {
            response.setPerformative(ACLMessage.AGREE);
        } else {
            throw new RefuseException("Action no valid");
        }

        return response;
    }

    @Override
    protected final ACLMessage prepareInformResultResponse(ACLMessage request, ACLMessage response) throws FailureException {
        try {
            Action action = (Action) contentManager.extractContent(request);
            response.setPerformative(ACLMessage.INFORM);
            contentManager.fillContent(response, performAction(action));
            return response;
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }
    }

    public boolean isValidAction(Action action) {
        return true;
    }

    public Predicate performAction(Action action) throws FailureException {
        return null;
    }

}
