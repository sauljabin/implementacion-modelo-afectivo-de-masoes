/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental;

import agent.AgentLogger;
import agent.AgentProtocolAssistant;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class FunctionalTest extends Behaviour {

    private static final int TIMEOUT = 4000;
    private static final String PASSED = "PASSED";
    private static final String FAILED = "FAILED";
    private List<AID> agentsToKill;
    private AgentLogger logger;
    private AgentProtocolAssistant agentProtocolAssistant;
    private boolean passed;
    private String failedMessage;

    @Override
    public void onStart() {
        passed = true;
        failedMessage = "";
        agentsToKill = new ArrayList<>();
        logger = new AgentLogger(LoggerFactory.getLogger(FunctionalTest.class));
        agentProtocolAssistant = new AgentProtocolAssistant(myAgent, TIMEOUT);
        try {
            setUp();
        } catch (Exception e) {
            failed(e.getMessage().trim());
        }
    }

    @Override
    public int onEnd() {
        logResult();
        getTesterAgent().registerTest(passed);
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

    private TesterAgent getTesterAgent() {
        return (TesterAgent) myAgent;
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

    public abstract void performTest();

    @Override
    public void action() {
        try {
            performTest();
        } catch (AssertionError | Exception e) {
            failed(e.getMessage().trim());
        }
    }

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

}
