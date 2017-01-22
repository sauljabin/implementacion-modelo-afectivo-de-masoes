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
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logger.jade.JadeLogger;
import org.slf4j.LoggerFactory;

public class OntologyResponderBehaviour extends ProtocolResponderBehaviour {

    private final MessageTemplate messageTemplate;
    private ContentManager contentManager;
    private Ontology ontology;
    private JadeLogger logger;

    public OntologyResponderBehaviour(Agent agent, MessageTemplate messageTemplate, Ontology ontology) {
        super(agent, messageTemplate);
        this.messageTemplate = messageTemplate;
        this.ontology = ontology;
        contentManager = new ContentManager();
        logger = new JadeLogger(LoggerFactory.getLogger(OntologyResponderBehaviour.class));
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
    protected ACLMessage prepareAcceptanceResponse(ACLMessage request) {
        contentManager.registerLanguage(new SLCodec());
        contentManager.registerOntology(ontology);
        ACLMessage response = request.createReply();
        try {
            Action action = (Action) contentManager.extractContent(request);
            if (isValidAction(action)) {
                response.setPerformative(ACLMessage.AGREE);
            } else {
                response.setPerformative(ACLMessage.REFUSE);
                response.setContent("Action no valid");
            }
        } catch (Exception e) {
            logger.exception(myAgent, e);
            response.setPerformative(ACLMessage.REFUSE);
            response.setContent(e.getMessage());
        }
        return response;
    }

    @Override
    protected ACLMessage prepareInformResultResponse(ACLMessage request, ACLMessage response) {
        try {
            Action action = (Action) contentManager.extractContent(request);
            response.setPerformative(ACLMessage.INFORM);
            contentManager.fillContent(response, performAction(action));
        } catch (Exception e) {
            logger.exception(myAgent, e);
            response.setPerformative(ACLMessage.FAILURE);
            response.setContent(e.getMessage());
        }
        return response;
    }

    public boolean isValidAction(Action action) {
        return true;
    }

    public Predicate performAction(Action action) {
        return null;
    }

}
