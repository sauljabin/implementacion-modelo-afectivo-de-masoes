/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import logger.LogWriter;
import logger.LoggerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentLogger {

    private Logger logger;
    private LoggerHandler loggerHandler;
    private Agent agent;

    public AgentLogger(Agent agent) {
        this.agent = agent;
        logger = LoggerFactory.getLogger(agent.getClass());
    }

    public void exception(Exception exception) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Exception in agent \"%s\", class \"%s\": %s")
                .args(agent.getLocalName(), agent.getClass().getName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void messageRequest(ACLMessage message) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Message request in agent \"%s\": %s")
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void messageResponse(ACLMessage message) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Message response in agent \"%s\": %s")
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void agentInfo() {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Agent %s: %s")
                .args(agent.getLocalName(), agent)
                .info(logger);
    }

    public void info(String info) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Agent %s: %s")
                .args(agent.getLocalName(), info)
                .info(logger);
    }

    public void setLoggerHandler(LoggerHandler loggerHandler) {
        this.loggerHandler = loggerHandler;
    }

}
