/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test;

import agent.AgentProtocolAssistant;
import agent.TimeoutException;
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

import java.util.Vector;

public abstract class FunctionalTest {

    private static JadeBoot jadeBoot;
    private static Agent testerAgent;
    private static AgentProtocolAssistant agentProtocolAssistant;
    private static Vector<AID> agentsToKill;

    @BeforeClass
    public static void setUpJade() throws Exception {
        if (jadeBoot != null) {
            return;
        }

        JadeSettings.getInstance().set(JadeSettings.GUI, "false");
        ApplicationSettings.getInstance().set(ApplicationSettings.MASOES_ENV, "functional-tests");

        jadeBoot = new JadeBoot();
        jadeBoot.boot();

        testerAgent = new Agent();
        jadeBoot.addAgent("tester", testerAgent);

        agentProtocolAssistant = new AgentProtocolAssistant(testerAgent, 10000);
        agentsToKill = new Vector<>();
    }

    @AfterClass
    public static void tearDownJade() throws Exception {
        while (!agentsToKill.isEmpty()) {
            AID aid = agentsToKill.firstElement();
            try {
                agentProtocolAssistant.killAgent(aid);
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
        agentProtocolAssistant.killAgent(agentToKill);
        agentsToKill.remove(agentToKill);
    }

    public AID createAgent(Class<? extends Agent> agentClass) {
        AID agent = agentProtocolAssistant.createAgent(agentClass);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent() {
        AID agent = agentProtocolAssistant.createAgent();
        agentsToKill.add(agent);
        return agent;
    }

    public String addBehaviour(AID agent, Class<? extends Behaviour> behaviourClass) {
        return agentProtocolAssistant.addBehaviour(agent, behaviourClass);
    }

    public void removeBehaviour(AID agent, String behaviourName) {
        agentProtocolAssistant.removeBehaviour(agent, behaviourName);
    }

    public void sendMessage(ACLMessage message) {
        testerAgent.send(message);
    }

    public ACLMessage blockingReceive(long millis) {
        ACLMessage message = testerAgent.blockingReceive(millis);
        if (message == null) {
            throw new TimeoutException("Timeout receiving message");
        }
        return message;
    }

    public ACLMessage blockingReceive() {
        return blockingReceive(agentProtocolAssistant.getTimeout());
    }

    public void registerOntology(Ontology ontology) {
        agentProtocolAssistant.registerOntology(ontology);
    }

    public ContentElement sendActionAndWaitContent(AID receiver, AgentAction agentAction, String ontology) {
        return agentProtocolAssistant.sendActionAndWaitContent(receiver, agentAction, ontology);
    }

    public ACLMessage sendActionAndWaitMessage(AID receiver, AgentAction agentAction, String ontology) {
        return agentProtocolAssistant.sendActionAndWaitMessage(receiver, agentAction, ontology);
    }

    public ACLMessage createRequestMessage(AID receiver, String ontology) {
        return agentProtocolAssistant.createRequestMessage(receiver, ontology);
    }

}
