/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
import ontology.FillOntologyContentException;

public class MessageBuilder {

    private static final int RANDOM_STRING_LENGTH = 20;
    private AID sender;
    private int performative;
    private AID receiver;
    private Ontology ontology;
    private Codec language;
    private String protocol;
    private String conversationId;
    private String replyWith;
    private Object content;

    public MessageBuilder() {
        performative = ACLMessage.REQUEST;
    }

    public MessageBuilder sender(AID sender) {
        this.sender = sender;
        return this;
    }

    public MessageBuilder receiver(AID receiver) {
        this.receiver = receiver;
        return this;
    }

    public MessageBuilder performative(int performative) {
        this.performative = performative;
        return this;
    }

    public MessageBuilder ontology(Ontology ontology) {
        this.ontology = ontology;
        return this;
    }

    public MessageBuilder language(Codec language) {
        this.language = language;
        return this;
    }

    public MessageBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public MessageBuilder conversationId(String conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public MessageBuilder replyWith(String replyWith) {
        this.replyWith = replyWith;
        return this;
    }

    public MessageBuilder content(Object content) {
        this.content = content;
        return this;
    }

    public MessageBuilder fipaSL() {
        this.language = SemanticLanguage.getInstance();
        return this;
    }

    public MessageBuilder fipaRequest() {
        this.protocol = FIPANames.InteractionProtocol.FIPA_REQUEST;
        return this;
    }

    public MessageBuilder request() {
        this.performative = ACLMessage.REQUEST;
        return this;
    }

    public MessageBuilder conversationId() {
        StringGenerator stringGenerator = new StringGenerator();
        this.conversationId = stringGenerator.getString(RANDOM_STRING_LENGTH);
        return this;
    }

    public MessageBuilder replyWith() {
        StringGenerator stringGenerator = new StringGenerator();
        this.replyWith = stringGenerator.getString(RANDOM_STRING_LENGTH);
        return this;
    }

    public ACLMessage build() {
        ACLMessage message = new ACLMessage(performative);
        message.setSender(sender);
        message.addReceiver(receiver);
        message.setProtocol(protocol);
        message.setConversationId(conversationId);
        message.setReplyWith(replyWith);

        if (ontology != null) {
            message.setOntology(ontology.getName());
        }

        if (language != null) {
            message.setLanguage(language.getName());
        }

        if (content != null) {
            if (content instanceof String) {
                message.setContent((String) content);
            } else if (content instanceof ContentElement) {
                ContentManager contentManager = new ContentManager();
                contentManager.registerOntology(ontology);
                contentManager.registerLanguage(language);
                try {
                    contentManager.fillContent(message, (ContentElement) content);
                } catch (Exception e) {
                    throw new FillOntologyContentException(e);
                }
            }
        }

        return message;
    }

}
