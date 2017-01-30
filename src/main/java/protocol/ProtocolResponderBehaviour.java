/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import logger.writer.JadeLogger;
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
        try {
            logger.messageRequest(myAgent, request);
            ACLMessage response = prepareAcceptanceResponse(request);
            logger.messageResponse(myAgent, response);
            return response;
        } catch (Exception e) {
            ACLMessage response = request.createReply();
            if (e instanceof NotUnderstoodException) {
                response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            } else {
                response.setPerformative(ACLMessage.REFUSE);
            }
            return prepareResponseFromException(e, response);
        }
    }

    @Override
    protected final ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
        try {
            ACLMessage informResponse = prepareInformResultResponse(request, response);
            logger.messageResponse(myAgent, informResponse);
            return informResponse;
        } catch (Exception e) {
            response.setPerformative(ACLMessage.FAILURE);
            return prepareResponseFromException(e, response);
        }
    }

    private ACLMessage prepareResponseFromException(Exception exception, ACLMessage response) {
        response.setContent(exception.getMessage());
        logger.exception(myAgent, exception);
        logger.messageResponse(myAgent, response);
        return response;
    }

    protected ACLMessage prepareAcceptanceResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        return null;
    }

    protected ACLMessage prepareInformResultResponse(ACLMessage request, ACLMessage response) throws FailureException {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", getBehaviourName())
                .toString();
    }

}
