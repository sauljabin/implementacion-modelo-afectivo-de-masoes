/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package test;

import application.ApplicationSettings;
import jade.JadeBoot;
import jade.JadeSettings;
import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import protocol.ProtocolAssistant;
import protocol.TimeoutRequestException;

import java.util.Vector;

public abstract class FunctionalTest {

    private static JadeBoot jadeBoot;
    private static Agent testerAgent;
    private static ProtocolAssistant protocolAssistant;
    private static Vector<AID> agentsToKill;

    @BeforeClass
    public static void setUpJade() throws Exception {
        JadeSettings.getInstance().set(JadeSettings.GUI, "false");
        ApplicationSettings.getInstance().set(ApplicationSettings.MASOES_ENV, "functional-tests");

        if (jadeBoot != null) {
            return;
        }

        jadeBoot = new JadeBoot();
        jadeBoot.boot();

        testerAgent = new Agent();
        jadeBoot.addAgent("tester", testerAgent);

        protocolAssistant = new ProtocolAssistant(testerAgent, 10000);
        agentsToKill = new Vector<>();
    }

    @AfterClass
    public static void tearDownJade() throws Exception {
        while (!agentsToKill.isEmpty()) {
            AID aid = agentsToKill.firstElement();
            try {
                protocolAssistant.killAgent(aid);
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
        protocolAssistant.killAgent(agentToKill);
    }

    public AID createAgent(Class<? extends Agent> agentClass) {
        AID agent = protocolAssistant.createAgent(agentClass);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent() {
        AID agent = protocolAssistant.createAgent();
        agentsToKill.add(agent);
        return agent;
    }

    public String addBehaviour(AID agent, Class<? extends Behaviour> behaviourClass) {
        return protocolAssistant.addBehaviour(agent, behaviourClass);
    }

    public void removeBehaviour(AID agent, String behaviourName) {
        protocolAssistant.removeBehaviour(agent, behaviourName);
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

    public ACLMessage blockingReceive() {
        return blockingReceive(protocolAssistant.getTimeout());
    }

    public void registerOntology(Ontology ontology) {
        protocolAssistant.registerOntology(ontology);
    }

    public ContentElement sendActionAndWaitContent(AID receiver, Ontology ontology, AgentAction agentAction) {
        return protocolAssistant.sendActionAndWaitContent(receiver, ontology, agentAction);
    }

    public ACLMessage sendActionAndWaitMessage(AID receiver, Ontology ontology, AgentAction agentAction) {
        return protocolAssistant.sendActionAndWaitMessage(receiver, ontology, agentAction);
    }

    public ACLMessage createRequestMessage(AID receiver, Ontology ontology) {
        return protocolAssistant.createRequestMessage(receiver, ontology);
    }

}
