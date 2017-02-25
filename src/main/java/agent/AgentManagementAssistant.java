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
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AgentManagementAssistant {

    private static final int RANDOM_STRING_LENGTH = 30;
    private long timeout;
    private StringGenerator stringGenerator;
    private Agent agent;
    private OntologyAssistant ontologyAssistantManagement;
    private OntologyAssistant ontologyAssistantConfigurable;
    private ProtocolAssistant protocolAssistant;
    private OntologyAssistant ontologyAssistantFIPA;

    public AgentManagementAssistant(Agent agent) {
        this(agent, 5000);
    }

    public AgentManagementAssistant(Agent agent, long timeout) {
        this.agent = agent;
        this.timeout = timeout;
        stringGenerator = new StringGenerator();
        protocolAssistant = new ProtocolAssistant(agent, timeout);
        ontologyAssistantManagement = new OntologyAssistant(agent, JADEManagementOntology.getInstance());
        ontologyAssistantConfigurable = new OntologyAssistant(agent, ConfigurableOntology.getInstance());
        ontologyAssistantFIPA = new OntologyAssistant(agent, FIPAManagementOntology.getInstance());
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

    public void register(AID agentName, ServiceDescription... serviceDescriptions) {
        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.setName(agentName);
            Arrays.stream(serviceDescriptions).forEach(
                    serviceDescription -> agentDescription.addServices(serviceDescription)
            );
            DFService.register(agent, agentDescription);
        } catch (Exception e) {
            throw new InvalidResponseException(e);
        }
    }

    public void register(ServiceDescription... serviceDescriptions) {
        register(agent.getAID(), serviceDescriptions);
    }

    public List<ServiceDescription> services(AID agentName) {
        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.setName(agentName);
            SearchConstraints constraints = new SearchConstraints();
            constraints.setMaxResults(-1L);
            DFAgentDescription[] results = DFService.search(agent, agentDescription, constraints);

            if (results.length > 0) {
                DFAgentDescription description = results[0];
                List<ServiceDescription> list = new ArrayList<>();
                description.getAllServices().forEachRemaining(service -> list.add((ServiceDescription) service));
                return list;
            }

            return new ArrayList<>();
        } catch (Exception e) {
            throw new InvalidResponseException(e);
        }
    }

    public List<AID> search(ServiceDescription serviceDescription) {
        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.addServices(serviceDescription);
            SearchConstraints constraints = new SearchConstraints();
            constraints.setMaxResults(-1L);
            DFAgentDescription[] results = DFService.search(agent, agentDescription, constraints);
            return Arrays.stream(results).map(DFAgentDescription::getName).collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidResponseException(e);
        }
    }

    public List<AID> agents() {
        try {
            AMSAgentDescription amsAgentDescription = new AMSAgentDescription();
            SearchConstraints constraints = new SearchConstraints();
            constraints.setMaxResults(-1L);
            AMSAgentDescription[] results = AMSService.search(agent, amsAgentDescription, constraints);
            return Arrays.stream(results).filter(
                    description -> !description.getName().getLocalName().matches("ams|df")
            ).map(AMSAgentDescription::getName).collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidResponseException(e);
        }
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
