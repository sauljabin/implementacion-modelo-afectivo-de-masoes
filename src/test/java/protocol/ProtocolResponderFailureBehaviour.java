/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProtocolResponderFailureBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        myAgent.addBehaviour(new ProtocolResponderBehaviour(myAgent, MessageTemplate.MatchAll()) {
            @Override
            protected ACLMessage prepareAcceptanceResponse(ACLMessage request) {
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.AGREE);
                return reply;
            }

            @Override
            protected ACLMessage prepareInformResultResponse(ACLMessage request, ACLMessage response) throws FailureException {
                throw new FailureException("MESSAGE FAILURE");
            }
        });
    }

}
