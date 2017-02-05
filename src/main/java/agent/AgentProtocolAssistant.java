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
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import protocol.ExtractOntologyContentException;
import protocol.FillOntologyContentException;
import util.StopWatch;
import util.StringGenerator;

import java.util.List;

public class AgentProtocolAssistant {

    public static final int DEFAULT_TIMEOUT = 4000;
    private static final int RANDOM_STRING_LENGTH = 10;
    private ContentManager contentManager;
    private StringGenerator stringGenerator;
    private Agent agent;
    private long timeout;
    private StopWatch stopWatch;

    public AgentProtocolAssistant(Agent agent) {
        this(agent, DEFAULT_TIMEOUT);
    }

    public AgentProtocolAssistant(Agent agent, long timeout) {
        this.agent = agent;
        this.timeout = timeout;
        stringGenerator = new StringGenerator();
        stopWatch = new StopWatch();
        contentManager = new ContentManager();
        contentManager.registerLanguage(new SLCodec());
        contentManager.registerOntology(new ConfigurableOntology());
        contentManager.registerOntology(JADEManagementOntology.getInstance());
        contentManager.registerOntology(BasicOntology.getInstance());
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public void registerOntology(Ontology ontology) {
        contentManager.registerOntology(ontology);
    }

    public ACLMessage createRequestMessage(AID receiver, String ontology) {
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

    public void sendActionAndWaitDone(AID receiver, AgentAction agentAction, String ontology) {
        ContentElement contentElement = sendActionAndWaitContent(receiver, agentAction, ontology);

        if (!(contentElement instanceof Done)) {
            throw new InvalidResponseException("Unknown notification: " + contentElement);
        }
    }

    public ContentElement sendActionAndWaitContent(AID receiver, AgentAction agentAction, String ontology) {
        ACLMessage response = sendActionAndWaitMessage(receiver, agentAction, ontology);
        if (response.getPerformative() != ACLMessage.INFORM) {
            throw new InvalidResponseException("No inform response: " + ACLMessage.getPerformative(response.getPerformative()));
        }

        try {
            return contentManager.extractContent(response);
        } catch (Exception e) {
            throw new ExtractOntologyContentException(e);
        }
    }

    public ACLMessage sendActionAndWaitMessage(AID receiver, AgentAction agentAction, String ontology) {
        ACLMessage request = createRequestMessage(receiver, ontology);

        Action action = new Action(receiver, agentAction);

        try {
            contentManager.fillContent(request, action);
        } catch (Exception e) {
            throw new FillOntologyContentException(e);
        }

        agent.send(request);

        MessageTemplate messageTemplate = MessageTemplate.MatchInReplyTo(request.getReplyWith());

        stopWatch.reset();
        stopWatch.start();
        ACLMessage response = agent.blockingReceive(messageTemplate, timeout);
        stopWatch.stop();

        if (response == null) {
            throw new TimeoutException("The agent did not respond");
        }

        if (response.getPerformative() == ACLMessage.AGREE) {
            response = agent.blockingReceive(messageTemplate, timeout - stopWatch.getTime());
        }

        if (response == null) {
            throw new TimeoutException("The agent did not respond");
        }

        return response;
    }

    public void killAgent(AID agentToKill) {
        KillAgent content = new KillAgent();
        content.setAgent(agentToKill);
        sendActionAndWaitDone(agent.getAMS(), content, JADEManagementOntology.NAME);
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass, List<String> arguments) {
        CreateAgent content = new CreateAgent();
        content.setAgentName(agentName);
        content.setClassName(agentClass.getCanonicalName());
        content.setContainer((ContainerID) agent.here());

        if (arguments != null) {
            arguments.forEach(arg -> content.addArguments(arg));
        }

        sendActionAndWaitDone(agent.getAMS(), content, JADEManagementOntology.NAME);
        return agent.getAID(agentName);
    }

    public String addBehaviour(AID agent, String behaviourName, Class<? extends Behaviour> behaviourClass) {
        AddBehaviour content = new AddBehaviour(behaviourName, behaviourClass.getCanonicalName());
        sendActionAndWaitDone(agent, content, ConfigurableOntology.ONTOLOGY_NAME);
        return behaviourName;
    }

    public String addBehaviour(AID agent, Class<? extends Behaviour> behaviourClass) {
        return addBehaviour(agent, stringGenerator.getString(RANDOM_STRING_LENGTH), behaviourClass);
    }

    public void removeBehaviour(AID agent, String behaviourName) {
        RemoveBehaviour content = new RemoveBehaviour(behaviourName);
        sendActionAndWaitDone(agent, content, ConfigurableOntology.ONTOLOGY_NAME);
    }

    public void killContainer() {
        KillContainer content = new KillContainer();
        content.setContainer((ContainerID) agent.here());
        ACLMessage requestMessage = createRequestMessage(agent.getAMS(), JADEManagementOntology.NAME);
        Action action = new Action(agent.getAMS(), content);
        try {
            contentManager.fillContent(requestMessage, action);
        } catch (Exception e) {
            throw new FillOntologyContentException(e);
        }
        agent.send(requestMessage);
    }

    public void shutDownPlatform() {
        ShutdownPlatform content = new ShutdownPlatform();
        ACLMessage requestMessage = createRequestMessage(agent.getAMS(), JADEManagementOntology.NAME);
        Action action = new Action(agent.getAMS(), content);
        try {
            contentManager.fillContent(requestMessage, action);
        } catch (Exception e) {
            throw new FillOntologyContentException(e);
        }
        agent.send(requestMessage);
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
