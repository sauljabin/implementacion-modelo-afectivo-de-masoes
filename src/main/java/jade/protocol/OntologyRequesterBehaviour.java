/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.exception.ExtractOntologyContentException;
import jade.exception.FillOntologyContentException;
import jade.lang.acl.ACLMessage;
import logger.writer.JadeLogger;
import org.slf4j.LoggerFactory;
import string.random.StringGenerator;

public class OntologyRequesterBehaviour extends ProtocolRequesterBehaviour {

    private static final int CONVERSATION_ID_LENGTH = 10;
    private StringGenerator stringGenerator;
    private AID receiver;
    private Ontology ontology;
    private AgentAction agentAction;
    private ContentManager contentManager;
    private JadeLogger logger;

    public OntologyRequesterBehaviour(Agent agent, AID receiver, AgentAction agentAction, Ontology ontology) {
        super(agent, new ACLMessage(ACLMessage.REQUEST));
        this.receiver = receiver;
        this.ontology = ontology;
        this.agentAction = agentAction;
        contentManager = new ContentManager();
        logger = new JadeLogger(LoggerFactory.getLogger(OntologyRequesterBehaviour.class));
        stringGenerator = new StringGenerator();
    }

    public AID getReceiver() {
        return receiver;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public AgentAction getAgentAction() {
        return agentAction;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    @Override
    protected final ACLMessage prepareRequestInteraction(ACLMessage message) {
        contentManager.registerLanguage(new SLCodec());
        contentManager.registerOntology(ontology);

        message.setPerformative(ACLMessage.REQUEST);
        message.setOntology(ontology.getName());
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setConversationId(stringGenerator.getString(CONVERSATION_ID_LENGTH));
        message.setSender(myAgent.getAID());
        message.addReceiver(receiver);

        try {
            contentManager.fillContent(message, new Action(myAgent.getAID(), agentAction));
        } catch (Exception e) {
            logger.exception(myAgent, e);
            throw new FillOntologyContentException(e);
        }

        return message;
    }

    @Override
    protected final void handleAgree(ACLMessage message) {
        handleAgree(message.getContent());
    }

    protected void handleAgree(String contentMessage) {
    }

    @Override
    protected final void handleRefuse(ACLMessage message) {
        handleRefuse(message.getContent());
    }

    protected void handleRefuse(String contentMessage) {
    }

    @Override
    protected final void handleNotUnderstood(ACLMessage message) {
        handleNotUnderstood(message.getContent());
    }

    protected void handleNotUnderstood(String contentMessage) {
    }

    @Override
    protected final void handleFailure(ACLMessage message) {
        handleFailure(message.getContent());
    }

    protected void handleFailure(String contentMessage) {
    }

    @Override
    protected final void handleInform(ACLMessage message) {
        try {
            Predicate predicate = (Predicate) contentManager.extractContent(message);
            handleInform(predicate);
        } catch (Exception e) {
            logger.exception(myAgent, e);
            throw new ExtractOntologyContentException(e);
        }
    }

    protected void handleInform(Predicate predicate) {
    }

}
