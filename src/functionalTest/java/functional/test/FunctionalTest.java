/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test;

import agent.AgentLogger;
import agent.AgentProtocolAssistant;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class FunctionalTest extends Behaviour {

    private static final String PASSED = "PASSED";
    private static final String FAILED = "FAILED";
    private List<AID> agentsToKill;
    private AgentLogger logger;
    private AgentProtocolAssistant agentProtocolAssistant;
    private boolean passed;
    private String failedMessage;
    private long timeout;

    @Override
    public void onStart() {
        timeout = 5000L;
        passed = true;
        failedMessage = "";
        agentsToKill = new ArrayList<>();
        logger = new AgentLogger(LoggerFactory.getLogger(FunctionalTest.class));
        agentProtocolAssistant = new AgentProtocolAssistant(myAgent, timeout);
        try {
            setUp();
        } catch (Exception e) {
            failedFromException(e);
        }
    }

    @Override
    public void action() {
        try {
            performTest();
        } catch (AssertionError | Exception e) {
            failedFromException(e);
        }
    }

    private void failedFromException(Throwable e) {
        String line = "Unknown";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            if (e.getStackTrace()[i].getClassName().equals(getClass().getName())) {
                line = String.valueOf(e.getStackTrace()[i].getLineNumber());
                break;
            }
        }
        failed(String.format("Line: %s\n%s", line, e.getMessage().trim()));
    }

    @Override
    public int onEnd() {
        logResult();
        TesterAgent testerAgent = (TesterAgent) myAgent;
        testerAgent.registerTest(passed);
        agentsToKill.forEach(aid -> {
            try {
                killAgent(aid);
            } catch (Exception e) {
                logger.exception(myAgent, e);
            }
        });
        return 0;
    }

    private void logResult() {
        log("");
        log(String.format("%s > %s", getClass().getCanonicalName(), passed ? PASSED : FAILED));

        if (!passed && !Optional.ofNullable(failedMessage).orElse("").isEmpty()) {
            log(failedMessage);
        }
    }


    public void failed() {
        failed("");
    }

    public void failed(String message) {
        passed = false;
        failedMessage = message;
    }

    public void log(String message) {
        System.out.println(message);
    }

    public abstract void setUp();

    public abstract void performTest() throws Exception;

    @Override
    public boolean done() {
        return true;
    }

    public void killAgent(AID agentToKill) {
        agentProtocolAssistant.killAgent(agentToKill);
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass, List<String> arguments) {
        AID agent = agentProtocolAssistant.createAgent(agentName, agentClass, arguments);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent(Class<? extends Agent> agentClass, List<String> arguments) {
        AID agent = agentProtocolAssistant.createAgent(agentClass, arguments);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass) {
        AID agent = agentProtocolAssistant.createAgent(agentName, agentClass);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent(Class<? extends Agent> agentClass) {
        AID agent = agentProtocolAssistant.createAgent(agentClass);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent(List<String> arguments) {
        AID agent = agentProtocolAssistant.createAgent(arguments);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent(String agentName) {
        AID agent = agentProtocolAssistant.createAgent(agentName);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent(String agentName, List<String> arguments) {
        AID agent = agentProtocolAssistant.createAgent(agentName, arguments);
        agentsToKill.add(agent);
        return agent;
    }

    public AID createAgent() {
        AID agent = agentProtocolAssistant.createAgent();
        agentsToKill.add(agent);
        return agent;
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
        myAgent.send(message);
    }

    public ACLMessage blockingReceive(long millis) {
        return myAgent.blockingReceive(millis);
    }

    public ACLMessage blockingReceive() {
        return myAgent.blockingReceive(timeout);
    }

    public ACLMessage receive() {
        return myAgent.receive();
    }

    public AID getAID() {
        return myAgent.getAID();
    }

    public AID getAID(String name) {
        return myAgent.getAID(name);
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
        agentProtocolAssistant.setTimeout(timeout);
    }

}
