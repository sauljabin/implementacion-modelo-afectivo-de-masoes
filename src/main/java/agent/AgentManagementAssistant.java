/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Deregister;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Register;
import jade.domain.FIPAAgentManagement.Search;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;
import ontology.OntologyAssistant;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import protocol.InvalidResponseException;
import protocol.ProtocolAssistant;
import util.StopWatch;
import util.StringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentManagementAssistant {

    private static final int RANDOM_STRING_LENGTH = 30;
    private long timeout;
    private StringGenerator stringGenerator;
    private Agent agent;
    private OntologyAssistant ontologyAssistantManagement;
    private OntologyAssistant ontologyAssistantConfigurable;
    private OntologyAssistant ontologyAssistantFIPA;
    private ProtocolAssistant protocolAssistant;

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
        protocolAssistant.setTimeout(timeout);
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
        delay();
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
        delay();
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

    public void register(ServiceDescription... serviceDescriptions) {
        register(agent.getAID(), serviceDescriptions);
    }

    public void register(AID agentName, ServiceDescription... serviceDescriptions) {
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(agentName);
        Arrays.stream(serviceDescriptions).forEach(
                serviceDescription -> agentDescription.addServices(serviceDescription)
        );

        Register register = new Register();
        register.setDescription(agentDescription);

        sendRequestAndVerifyDone(agent.getDefaultDF(), register, ontologyAssistantFIPA);
        delay();
    }

    public void deRegister() {
        deRegister(agent.getAID());
    }

    public void deRegister(AID agentName) {
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(agentName);

        Deregister deregister = new Deregister();
        deregister.setDescription(agentDescription);

        sendRequestAndVerifyDone(agent.getDefaultDF(), deregister, ontologyAssistantFIPA);
        delay();
    }

    public List<ServiceDescription> services(AID agentName) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(agentName);

        List<ServiceDescription> serviceDescriptionList = new ArrayList<>();
        Iterator iterator = searchAgents(agent.getDefaultDF(), dfAgentDescription).getItems().iterator();

        while (iterator.hasNext()) {
            DFAgentDescription agentDescription = (DFAgentDescription) iterator.next();
            Iterator iteratorServices = agentDescription.getAllServices();
            while (iteratorServices.hasNext()) {
                serviceDescriptionList.add((ServiceDescription) iteratorServices.next());
            }
        }
        return serviceDescriptionList;
    }

    public List<AID> search(ServiceDescription serviceDescription) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);

        List<AID> listAID = new ArrayList<>();
        Iterator iterator = searchAgents(agent.getDefaultDF(), dfAgentDescription).getItems().iterator();

        while (iterator.hasNext()) {
            DFAgentDescription agentDescription = (DFAgentDescription) iterator.next();
            listAID.add(agentDescription.getName());
        }

        return listAID;
    }

    public List<AID> agents() {
        AMSAgentDescription amsAgentDescription = new AMSAgentDescription();

        List<AID> listAID = new ArrayList<>();
        Iterator iterator = searchAgents(agent.getAMS(), amsAgentDescription).getItems().iterator();

        while (iterator.hasNext()) {
            AMSAgentDescription agentDescription = (AMSAgentDescription) iterator.next();
            if (!agentDescription.getName().getLocalName().matches("ams|df")) {
                listAID.add(agentDescription.getName());
            }
        }

        return listAID;
    }

    private Result searchAgents(AID receiver, Object description) {
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);
        Search search = new Search();
        search.setDescription(description);
        search.setConstraints(constraints);

        ACLMessage requestAction = ontologyAssistantFIPA.createRequestAction(receiver, search);
        ACLMessage response = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);

        ContentElement contentElement = ontologyAssistantFIPA.extractMessageContent(response);

        if (!(contentElement instanceof Result)) {
            throw new InvalidResponseException("Unknown notification: " + contentElement);
        }

        return (Result) contentElement;
    }

    private void sendRequestAndVerifyDone(AID receiver, AgentAction content, OntologyAssistant ontologyAssistant) {
        ACLMessage requestAction = ontologyAssistant.createRequestAction(receiver, content);
        ACLMessage response = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);
        ContentElement contentElement = ontologyAssistant.extractMessageContent(response);
        if (!(contentElement instanceof Done)) {
            throw new InvalidResponseException("Unknown notification: " + contentElement);
        }
    }

    private void delay() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (stopWatch.getTime() < 150) {
            continue;
        }
        stopWatch.stop();
    }

}
