/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.configurable.ConfigurableOntology;
import protocol.ExtractOntologyContentException;
import protocol.FillOntologyContentException;
import util.StringGenerator;

import java.util.List;
import java.util.Optional;

public class AgentProtocolAssistant {

    public static final int DEFAULT_TIMEOUT = 4000;
    private static final int RANDOM_STRING_LENGTH = 10;
    private ContentManager contentManager;
    private StringGenerator stringGenerator;
    private Agent agent;
    private int timeout;

    public AgentProtocolAssistant(Agent agent) {
        this.agent = agent;
        timeout = DEFAULT_TIMEOUT;
        stringGenerator = new StringGenerator();
        contentManager = new ContentManager();
        contentManager.registerLanguage(new SLCodec());
        contentManager.registerOntology(JADEManagementOntology.getInstance());
        contentManager.registerOntology(new ConfigurableOntology());
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public ACLMessage createRequestMessage(String ontology, AID receiver) {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setSender(agent.getAID());
        message.addReceiver(receiver);
        message.setOntology(ontology);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setConversationId(stringGenerator.getString(RANDOM_STRING_LENGTH));
        message.setReplyWith(stringGenerator.getString(RANDOM_STRING_LENGTH));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        return message;
    }

    public void sendRequestAction(String ontology, AgentAction agentAction, AID receiver) {
        ACLMessage request = createRequestMessage(ontology, receiver);

        try {
            contentManager.fillContent(request, agentAction);
        } catch (Exception e) {
            throw new FillOntologyContentException(e);
        }

        agent.send(request);

        MessageTemplate messageTemplate = MessageTemplate.MatchInReplyTo(request.getReplyWith());
        ACLMessage response = agent.blockingReceive(messageTemplate, timeout);

        if (!Optional.ofNullable(response).isPresent()) {
            throw new TimeoutResponseException("The agent did not respond");
        }

        if (response.getPerformative() == ACLMessage.AGREE) {
            response = agent.blockingReceive(messageTemplate, timeout);
        }

        if (!Optional.ofNullable(response).isPresent()) {
            throw new TimeoutResponseException("The agent did not respond");
        }

        if (response.getPerformative() != ACLMessage.INFORM) {
            throw new InvalidResponseException("No inform response: " + ACLMessage.getPerformative(response.getPerformative()));
        }

        ContentElement contentElement;

        try {
            contentElement = contentManager.extractContent(response);
        } catch (Exception e) {
            throw new ExtractOntologyContentException(e);
        }

        if (!(contentElement instanceof Done)) {
            throw new InvalidResponseException("Unknown notification: " + response.getContent());
        }
    }

    public void killContainer() {
        try {
            ACLMessage request = createRequestMessage(JADEManagementOntology.NAME, agent.getAMS());
            KillContainer content = new KillContainer();
            content.setContainer((ContainerID) agent.here());
            contentManager.fillContent(request, content);
            agent.send(request);
        } catch (Exception e) {
            throw new FillOntologyContentException(e);
        }
    }

    public void killAgent(AID aid) {
        KillAgent content = new KillAgent();
        content.setAgent(aid);
        sendRequestAction(JADEManagementOntology.NAME, content, agent.getAMS());
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass, List<String> arguments) {
        CreateAgent content = new CreateAgent();
        content.setAgentName(agentName);
        content.setClassName(agentClass.getCanonicalName());
        content.setContainer((ContainerID) agent.here());

        if (Optional.ofNullable(arguments).isPresent()) {
            arguments.forEach(arg -> content.addArguments(arg));
        }

        sendRequestAction(JADEManagementOntology.NAME, content, agent.getAMS());
        return agent.getAID(agentName);
    }

    public AID createAgent(Class<? extends Agent> agentClass, List<String> arguments) {
        return createAgent(stringGenerator.getString(RANDOM_STRING_LENGTH), agentClass, arguments);
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass) {
        return createAgent(agentName, agentClass, null);
    }

    public AID createAgent(Class<? extends Agent> agentClass) {
        return createAgent(stringGenerator.getString(RANDOM_STRING_LENGTH), agentClass);
    }

    public AID createAgent(List<String> arguments) {
        return createAgent(stringGenerator.getString(RANDOM_STRING_LENGTH), ConfigurableAgent.class, arguments);
    }

    public AID createAgent(String agentName) {
        return createAgent(agentName, ConfigurableAgent.class, null);
    }

    public AID createAgent(String agentName, List<String> arguments) {
        return createAgent(agentName, ConfigurableAgent.class, arguments);
    }

    public AID createAgent() {
        return createAgent(stringGenerator.getString(RANDOM_STRING_LENGTH), ConfigurableAgent.class, null);
    }

}
