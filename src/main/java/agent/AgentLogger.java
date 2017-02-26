/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import util.LogWriter;

public class AgentLogger {

    private Logger logger;

    public AgentLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void exception(Agent agent, Exception exception) {
        new LogWriter()
                .message("Exception in agent \"%s\", class \"%s\": %s")
                .args(agent.getLocalName(), agent.getClass().getName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void messageRequest(Agent agent, ACLMessage message) {
        new LogWriter()
                .message("Message request in agent \"%s\": %s")
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void messageResponse(Agent agent, ACLMessage message) {
        new LogWriter()
                .message("Message response in agent \"%s\": %s")
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void agent(Agent agent) {
        new LogWriter()
                .message("Agent %s: %s")
                .args(agent.getLocalName(), agent)
                .info(logger);
    }

}
