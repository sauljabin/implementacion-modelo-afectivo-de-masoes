/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import logger.LogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import translate.Translation;

public class AgentLogger {

    private Logger logger;
    private Agent agent;
    private Translation translation;

    public AgentLogger(Agent agent) {
        this.agent = agent;
        logger = LoggerFactory.getLogger(agent.getClass());
        translation = Translation.getInstance();
    }

    public void exception(Exception exception) {
        new LogWriter()
                .message(translation.get("log.exception_in_agent"))
                .args(agent.getLocalName(), agent.getClass().getName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void messageRequest(ACLMessage message) {
        new LogWriter()
                .message(translation.get("log.request_message"))
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void messageResponse(ACLMessage message) {
        new LogWriter()
                .message(translation.get("log.response_message"))
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void agentInfo() {
        new LogWriter()
                .message(translation.get("log.agent"))
                .args(agent.getLocalName(), agent)
                .info(logger);
    }

    public void info(String info) {
        new LogWriter()
                .message(translation.get("log.agent"))
                .args(agent.getLocalName(), info)
                .info(logger);
    }

}
