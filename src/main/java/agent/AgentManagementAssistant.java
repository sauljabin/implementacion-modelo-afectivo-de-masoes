/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import ontology.OntologyAssistant;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import protocol.InvalidResponseException;
import protocol.ProtocolAssistant;
import util.StringGenerator;

import java.util.List;

public class AgentManagementAssistant {

    private static final int RANDOM_STRING_LENGTH = 30;
    private StringGenerator stringGenerator;
    private Agent agent;
    private OntologyAssistant ontologyAssistantManagement;
    private long timeout;
    private OntologyAssistant ontologyAssistantConfigurable;
    private ProtocolAssistant protocolAssistant;

    public AgentManagementAssistant(Agent agent) {
        this(agent, 5000);
    }

    public AgentManagementAssistant(Agent agent, long timeout) {
        this.agent = agent;
        this.timeout = timeout;
        stringGenerator = new StringGenerator();
        ontologyAssistantManagement = new OntologyAssistant(agent, JADEManagementOntology.getInstance());
        ontologyAssistantConfigurable = new OntologyAssistant(agent, ConfigurableOntology.getInstance());
        protocolAssistant = new ProtocolAssistant(agent, timeout);
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void shutdownPlatform() {
        ShutdownPlatform content = new ShutdownPlatform();
        ACLMessage message = ontologyAssistantManagement.createRequestAction(agent.getAMS(), content);
        agent.send(message);
    }

    public void killContainer() {
        KillContainer content = new KillContainer();
        content.setContainer((ContainerID) agent.here());
        ACLMessage message = ontologyAssistantManagement.createRequestAction(agent.getAMS(), content);
        agent.send(message);
    }

    public void killAgent(AID agentToKill) {
        KillAgent content = new KillAgent();
        content.setAgent(agentToKill);
        sendRequestAndVerifyDone(agent.getAMS(), content, ontologyAssistantManagement);
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass, List<String> arguments) {
        CreateAgent content = new CreateAgent();
        content.setAgentName(agentName);
        content.setClassName(agentClass.getCanonicalName());
        content.setContainer((ContainerID) agent.here());

        if (arguments != null) {
            arguments.forEach(arg -> content.addArguments(arg));
        }

        sendRequestAndVerifyDone(agent.getAMS(), content, ontologyAssistantManagement);
        return agent.getAID(agentName);
    }

    public AID createAgent(Class<? extends Agent> agentClass, List<String> arguments) {
        return createAgent(stringGenerator.getString(RANDOM_STRING_LENGTH).toLowerCase(), agentClass, arguments);
    }

    public void removeBehaviour(AID receiver, String behaviourName) {
        RemoveBehaviour content = new RemoveBehaviour(behaviourName);
        sendRequestAndVerifyDone(receiver, content, ontologyAssistantConfigurable);
    }

    public String addBehaviour(AID receiver, String behaviourName, Class<? extends Behaviour> behaviourClass) {
        AddBehaviour content = new AddBehaviour(behaviourName, behaviourClass.getCanonicalName());
        sendRequestAndVerifyDone(receiver, content, ontologyAssistantConfigurable);
        return behaviourName;
    }

    public String addBehaviour(AID agent, Class<? extends Behaviour> behaviourClass) {
        return addBehaviour(agent, stringGenerator.getString(RANDOM_STRING_LENGTH).toLowerCase(), behaviourClass);
    }

    private void sendRequestAndVerifyDone(AID receiver, AgentAction content, OntologyAssistant ontologyAssistant) {
        ACLMessage requestAction = ontologyAssistant.createRequestAction(receiver, content);
        ACLMessage response = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);
        ContentElement contentElement = ontologyAssistant.extractMessageContent(response);
        if (!(contentElement instanceof Done)) {
            throw new InvalidResponseException("Unknown notification: " + contentElement);
        }
    }

}
