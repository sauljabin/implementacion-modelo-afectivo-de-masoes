/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger.jade;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import logger.writer.LogWriter;
import org.slf4j.Logger;

public class JadeLogger {

    private Logger logger;

    public JadeLogger(Logger logger) {
        this.logger = logger;
    }

    public void exception(Agent agent, Exception exception) {
        new LogWriter()
                .message("Exception in agent \"%s\": %s")
                .args(agent.getLocalName(), exception.getMessage())
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
