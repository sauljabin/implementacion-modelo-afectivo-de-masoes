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

public class ProtocolResponderInformBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        myAgent.addBehaviour(new ProtocolResponderBehaviour(myAgent, MessageTemplate.MatchAll()) {
            @Override
            protected ACLMessage prepareAcceptanceResponse(ACLMessage request) {
                ACLMessage response = request.createReply();
                response.setPerformative(ACLMessage.AGREE);
                return response;
            }

            @Override
            protected ACLMessage prepareInformResultResponse(ACLMessage request, ACLMessage response) throws FailureException {
                response.setContent("INFORM CONTENT");
                response.setPerformative(ACLMessage.INFORM);
                return response;
            }
        });
    }

}
