/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.ContentManager;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.lang.acl.ACLMessage;
import protocol.FillOntologyContentException;
import util.StringGenerator;

import java.util.Arrays;
import java.util.List;

public class AgentMessageAssistant {

    private static final int CONVERSATION_ID_LENGTH = 10;
    private final ContentManager contentManager;
    private final StringGenerator stringGenerator;
    private Agent agent;

    public AgentMessageAssistant(Agent agent) {
        this.agent = agent;
        contentManager = new ContentManager();
        stringGenerator = new StringGenerator();
    }

    public void killContainer() {
        try {
            ACLMessage message = createRequestMessage(JADEManagementOntology.NAME, FIPANames.ContentLanguage.FIPA_SL0, agent.getAMS());
            KillContainer content = new KillContainer();
            content.setContainer((ContainerID) agent.here());
            contentManager.fillContent(message, content);
            agent.send(message);
        } catch (Exception e) {
            throw new FillOntologyContentException(e);
        }
    }

    private ACLMessage createRequestMessage(String ontology, String language, AID receiver) {
        return createRequestMessage(ontology, language, Arrays.asList(receiver));
    }

    private ACLMessage createRequestMessage(String ontology, String language, List<AID> receivers) {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setSender(agent.getAID());
        receivers.forEach(receiver -> message.addReceiver(receiver));
        message.setOntology(ontology);
        message.setLanguage(language);
        message.setConversationId(stringGenerator.getString(CONVERSATION_ID_LENGTH));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        return message;
    }

}
