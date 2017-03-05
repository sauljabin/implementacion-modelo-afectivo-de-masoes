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

public class AgentLogger {
    // TODO PASAR CLASE POR CONTRUCTOR
    private Logger logger;
    private LoggerHandler loggerHandler;

    public AgentLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void exception(Agent agent, Exception exception) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Exception in agent \"%s\", class \"%s\": %s")
                .args(agent.getLocalName(), agent.getClass().getName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void messageRequest(Agent agent, ACLMessage message) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Message request in agent \"%s\": %s")
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void messageResponse(Agent agent, ACLMessage message) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Message response in agent \"%s\": %s")
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void agent(Agent agent) {
        new LogWriter()
                .addLoggerHandler(loggerHandler)
                .message("Agent %s: %s")
                .args(agent.getLocalName(), agent)
                .info(logger);
    }

    public void info(Agent agent, String info) {
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
