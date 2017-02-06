/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
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
import util.MessageBuilder;

public class OntologyAssistant {

    private Agent agent;

    public OntologyAssistant(Agent agent) {
        this.agent = agent;
    }

    public ACLMessage createRequestMessage(AID receiver, Ontology ontology, AgentAction agentAction) {
        Action action = new Action(receiver, agentAction);
        return new MessageBuilder().request()
                .sender(agent.getAID()).receiver(receiver)
                .ontology(ontology).fipaSL().fipaRequest()
                .conversationId().replyWith()
                .content(action)
                .build();
    }

    public ContentElement extractContentMessage(Ontology ontology, ACLMessage message) {
        ContentManager contentManager = new ContentManager();
        contentManager.registerOntology(ontology);
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        try {
            return contentManager.extractContent(message);
        } catch (Exception e) {
            throw new ExtractOntologyContentException(e);
        }
    }

}
