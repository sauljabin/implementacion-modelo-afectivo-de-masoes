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
import jade.wrapper.AgentContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.List;

public abstract class FunctionalTest {

    private static ApplicationSettings applicationSettings = ApplicationSettings.getInstance();
    private static JadeSettings jadeSettings = JadeSettings.getInstance();
    private static JadeBoot jadeBoot;
    private static Agent testerAgent;
    private static AgentProtocolAssistant agentProtocolAssistant;

    @BeforeClass
    public static void setUpJade() throws Exception {
        jadeSettings.set(JadeSettings.GUI, "false");
        applicationSettings.set(ApplicationSettings.MASOES_ENV, "functional-tests");
        jadeBoot = new JadeBoot();
        jadeBoot.boot();
        testerAgent = new Agent();
        jadeBoot.addAgent("tester", testerAgent);
        agentProtocolAssistant = new AgentProtocolAssistant(testerAgent, 10000);
    }

    @AfterClass
    public static void tearDownJade() throws Exception {
        jadeBoot.kill();
        Thread.sleep(1500);
    }

    public static AgentContainer getContainer() {
        return jadeBoot.getMainContainer();
    }

    public void killAgent(AID agentToKill) {
        agentProtocolAssistant.killAgent(agentToKill);
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass, List<String> arguments) {
        return agentProtocolAssistant.createAgent(agentName, agentClass, arguments);
    }

    public AID createAgent(Class<? extends Agent> agentClass, List<String> arguments) {
        return agentProtocolAssistant.createAgent(agentClass, arguments);
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass) {
        return agentProtocolAssistant.createAgent(agentName, agentClass);
    }

    public AID createAgent(Class<? extends Agent> agentClass) {
        return agentProtocolAssistant.createAgent(agentClass);
    }

    public AID createAgent(List<String> arguments) {
        return agentProtocolAssistant.createAgent(arguments);
    }

    public AID createAgent(String agentName) {
        return agentProtocolAssistant.createAgent(agentName);
    }

    public AID createAgent(String agentName, List<String> arguments) {
        return agentProtocolAssistant.createAgent(agentName, arguments);
    }

    public AID createAgent() {
        return agentProtocolAssistant.createAgent();
    }

    public String addBehaviour(AID agent, String behaviourName, Class<? extends Behaviour> behaviourClass) {
        return agentProtocolAssistant.addBehaviour(agent, behaviourName, behaviourClass);
    }

    public String addBehaviour(AID agent, Class<? extends Behaviour> behaviourClass) {
        return agentProtocolAssistant.addBehaviour(agent, behaviourClass);
    }

    public void removeBehaviour(AID agent, String behaviourName) {
        agentProtocolAssistant.removeBehaviour(agent, behaviourName);
    }

    public ACLMessage createRequestMessage(AID receiver, String ontology) {
        return agentProtocolAssistant.createRequestMessage(receiver, ontology);
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

    public ACLMessage receive() {
        return testerAgent.receive();
    }

    public AID getAID() {
        return testerAgent.getAID();
    }

    public AID getAID(String name) {
        return testerAgent.getAID(name);
    }

    public ContentElement sendActionAndWaitContent(AID receiver, AgentAction agentAction, String ontology) {
        return agentProtocolAssistant.sendActionAndWaitContent(receiver, agentAction, ontology);
    }

    public ACLMessage sendActionAndWaitMessage(AID receiver, AgentAction agentAction, String ontology) {
        return agentProtocolAssistant.sendActionAndWaitMessage(receiver, agentAction, ontology);
    }

    public void registerOntology(Ontology ontology) {
        agentProtocolAssistant.registerOntology(ontology);
    }

}
