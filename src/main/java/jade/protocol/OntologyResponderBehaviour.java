/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OntologyResponderBehaviour extends ProtocolResponderBehaviour {

    public OntologyResponderBehaviour(Agent agent) {
        super(agent);
    }

    public OntologyResponderBehaviour(Agent agent, MessageTemplate messageTemplate) {
        super(agent, messageTemplate);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) {
        ACLMessage response = request.createReply();
        try {
            Action action = (Action) myAgent.getContentManager().extractContent(request);
            if (isValidAction(action)) {
                response.setPerformative(ACLMessage.AGREE);
            } else {
                response.setPerformative(ACLMessage.REFUSE);
                response.setContent("Action no valid");
            }
        } catch (Exception e) {
            response.setPerformative(ACLMessage.REFUSE);
            response.setContent(e.getMessage());
        }
        return response;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
        try {
            Action action = (Action) myAgent.getContentManager().extractContent(request);
            response.setPerformative(ACLMessage.INFORM);
            myAgent.getContentManager().fillContent(response, performAction(action));
        } catch (Exception e) {
            response.setPerformative(ACLMessage.FAILURE);
            response.setContent(e.getMessage());
        }
        return response;
    }

    public boolean isValidAction(Action action) {
        return true;
    }

    public Predicate performAction(Action action) {
        return null;
    }

}
