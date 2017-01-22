/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import logger.jade.JadeLogger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.LoggerFactory;

public class ProtocolResponderBehaviour extends SimpleAchieveREResponder {

    private JadeLogger logger;

    public ProtocolResponderBehaviour(Agent agent, MessageTemplate messageTemplate) {
        super(agent, messageTemplate);
        logger = new JadeLogger(LoggerFactory.getLogger(ProtocolResponderBehaviour.class));
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        reset(messageTemplate);
    }

    @Override
    protected final ACLMessage prepareResponse(ACLMessage request) {
        logger.messageRequest(myAgent, request);
        ACLMessage response = prepareAcceptanceResponse(request);
        logger.messageResponse(myAgent, response);
        return response;
    }

    protected ACLMessage prepareAcceptanceResponse(ACLMessage request) {
        return null;
    }

    @Override
    protected final ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
        ACLMessage informResponse = prepareInformResultResponse(request, response);
        logger.messageResponse(myAgent, informResponse);
        return informResponse;
    }

    protected ACLMessage prepareInformResultResponse(ACLMessage request, ACLMessage response) {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", getBehaviourName())
                .toString();
    }

}
