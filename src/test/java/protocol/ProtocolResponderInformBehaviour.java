/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProtocolResponderInformBehaviour extends ProtocolResponderBehaviour {

    public ProtocolResponderInformBehaviour() {
        super(null, MessageTemplate.MatchAll());
    }

    @Override
    protected ACLMessage prepareAcceptanceResponse(ACLMessage request) {
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.AGREE);
        return reply;
    }

    @Override
    protected ACLMessage prepareInformResultResponse(ACLMessage request, ACLMessage response) throws FailureException {
        response.setContent("INFORM CONTENT");
        response.setPerformative(ACLMessage.INFORM);
        return response;
    }

}
