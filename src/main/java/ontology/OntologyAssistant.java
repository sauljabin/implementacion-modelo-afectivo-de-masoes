/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
import protocol.ProtocolAssistant;
import util.MessageBuilder;

public class OntologyAssistant {

    private Agent agent;
    private Ontology ontology;
    private ContentManager contentManager;
    private ProtocolAssistant protocolAssistant;

    public OntologyAssistant(Agent agent, Ontology ontology) {
        this.agent = agent;
        this.ontology = ontology;
        contentManager = new ContentManager();
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        contentManager.registerOntology(ontology);

        protocolAssistant = new ProtocolAssistant(agent);
    }

    public long getTimeout() {
        return protocolAssistant.getTimeout();
    }

    public void setTimeout(long timeout) {
        protocolAssistant.setTimeout(timeout);
    }

    public ContentElement sendRequestAction(AID receiver, AgentAction agentAction) {
        ACLMessage message = protocolAssistant.sendRequest(createRequestAction(receiver, agentAction), ACLMessage.INFORM);
        return extractMessageContent(message);
    }

    public ACLMessage createRequestAction(AID receiver, AgentAction agentAction) {
        Action action = new Action(receiver, agentAction);
        return createRequestMessage(receiver, action);
    }

    private ACLMessage createRequestMessage(AID receiver, ContentElement contentElement) {
        return new MessageBuilder()
                .request()
                .sender(agent.getAID())
                .receiver(receiver)
                .ontology(ontology)
                .fipaSL()
                .fipaRequest()
                .conversationId()
                .replyWith()
                .content(contentElement)
                .build();
    }

    public void fillMessageContent(ACLMessage message, ContentElement contentElement) {
        try {
            contentManager.fillContent(message, contentElement);
        } catch (Exception e) {
            throw new FillOntologyContentException(e);
        }
    }

    public ContentElement extractMessageContent(ACLMessage message) {
        try {
            return contentManager.extractContent(message);
        } catch (Exception e) {
            throw new ExtractOntologyContentException(e);
        }
    }

}
