/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import logger.jade.JadeLogger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public class ProtocolRequesterBehaviour extends SimpleAchieveREInitiator {

    private JadeLogger logger;

    public ProtocolRequesterBehaviour(Agent agent, ACLMessage message) {
        super(agent, message);
        logger = new JadeLogger(LoggerFactory.getLogger(ProtocolRequesterBehaviour.class));
    }

    public void setMessage(ACLMessage message) {
        reset(message);
    }

    @Override
    protected final ACLMessage prepareRequest(ACLMessage request) {
        ACLMessage requestInteraction = prepareRequestInteraction(request);
        logger.messageRequest(myAgent, requestInteraction);
        return requestInteraction;
    }

    protected ACLMessage prepareRequestInteraction(ACLMessage request) {
        return null;
    }

    @Override
    protected void handleAllResponses(Vector messages) {
        for (int i = 0; i < messages.size(); i++) {
            logger.messageResponse(myAgent, (ACLMessage) messages.get(i));
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector messages) {
        for (int i = 0; i < messages.size(); i++) {
            logger.messageResponse(myAgent, (ACLMessage) messages.get(i));
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", getBehaviourName())
                .toString();
    }

}
