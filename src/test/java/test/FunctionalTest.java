/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package test;

import agent.AgentManagementAssistant;
import application.ApplicationSettings;
import jade.JadeBoot;
import jade.JadeSettings;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import ontology.OntologyAssistant;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import protocol.ProtocolAssistant;
import protocol.TimeoutRequestException;

import java.util.List;
import java.util.Vector;

public abstract class FunctionalTest {

    private static JadeBoot jadeBoot;
    private static Agent testerAgent;
    private static Vector<AID> agentsToKill;
    private static AgentManagementAssistant agentManagementAssistant;

    @BeforeClass
    public static void setUpJade() {
        JadeSettings.getInstance().set(JadeSettings.GUI, "false");
        ApplicationSettings.getInstance().set(ApplicationSettings.MASOES_ENV, "functional-tests");

        if (jadeBoot != null) {
            return;
        }

        jadeBoot = new JadeBoot();
        jadeBoot.boot();

        testerAgent = new Agent();
        jadeBoot.addAgent("tester", testerAgent);

        agentManagementAssistant = new AgentManagementAssistant(testerAgent);
        agentsToKill = new Vector<>();
    }

    @AfterClass
    public static void tearDownJade() {
        while (!agentsToKill.isEmpty()) {
            AID aid = agentsToKill.firstElement();
            try {
                agentManagementAssistant.killAgent(aid);
            } catch (Exception e) {
                System.out.println("Fail kill: " + aid);
            }
            agentsToKill.remove(aid);
            System.out.println("Kill: " + aid);
        }
    }

    public static AgentController getAgent(String name) {
        return jadeBoot.getAgent(name);
    }

    public void killAgent(AID agentToKill) {
        agentsToKill.remove(agentToKill);
        agentManagementAssistant.killAgent(agentToKill);
    }

    public AID createAgent(Class<? extends Agent> agentClass, List<String> arguments) {
        AID agent = agentManagementAssistant.createAgent(agentClass, arguments);
        agentsToKill.add(agent);
        return agent;
    }

    public String addBehaviour(AID agent, Class<? extends Behaviour> behaviourClass) {
        return agentManagementAssistant.addBehaviour(agent, behaviourClass);
    }

    public void removeBehaviour(AID agent, String behaviourName) {
        agentManagementAssistant.removeBehaviour(agent, behaviourName);
    }

    public void sendMessage(ACLMessage message) {
        testerAgent.send(message);
    }

    public ACLMessage blockingReceive(long millis) {
        ACLMessage message = testerAgent.blockingReceive(millis);
        if (message == null) {
            throw new TimeoutRequestException("Timeout receiving message");
        }
        return message;
    }

    public AID getAMS() {
        return testerAgent.getAMS();
    }

    public ContainerID getContainerID() {
        return (ContainerID) testerAgent.here();
    }

    public ACLMessage blockingReceive() {
        return blockingReceive(agentManagementAssistant.getTimeout());
    }

    public AID getAID() {
        return testerAgent.getAID();
    }

    public AID getAID(String name) {
        return testerAgent.getAID(name);
    }

    public ProtocolAssistant createProtocolAssistant() {
        return new ProtocolAssistant(testerAgent, agentManagementAssistant.getTimeout());
    }

    public OntologyAssistant createOntologyAssistant(Ontology ontology) {
        return new OntologyAssistant(testerAgent, ontology);
    }

}
