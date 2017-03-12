/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import agent.AgentLogger;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import util.ToStringBuilder;

import java.util.Vector;

public class ProtocolRequesterBehaviour extends SimpleAchieveREInitiator {

    private AgentLogger logger;
    private ACLMessage message;

    public ProtocolRequesterBehaviour(Agent agent, ACLMessage message) {
        super(agent, message);
        this.message = message;
        logger = new AgentLogger(agent);
    }

    public ACLMessage getMessage() {
        return message;
    }

    public void setMessage(ACLMessage message) {
        this.message = message;
        reset(message);
    }

    @Override
    protected final ACLMessage prepareRequest(ACLMessage request) {
        ACLMessage requestInteraction = prepareRequestInteraction(request);
        logger.messageRequest(requestInteraction);
        return requestInteraction;
    }

    protected ACLMessage prepareRequestInteraction(ACLMessage request) {
        return request;
    }

    @Override
    protected void handleAllResponses(Vector messages) {
        for (int i = 0; i < messages.size(); i++) {
            logger.messageResponse((ACLMessage) messages.get(i));
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector messages) {
        for (int i = 0; i < messages.size(); i++) {
            logger.messageResponse((ACLMessage) messages.get(i));
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", getBehaviourName())
                .toString();
    }

}
