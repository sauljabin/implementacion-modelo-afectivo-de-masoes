/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class SimpleBehaviour extends Behaviour {

    @Override
    public void action() {
        ACLMessage receive = myAgent.receive();
        if (receive != null) {
            ACLMessage reply = receive.createReply();
            reply.setPerformative(ACLMessage.CONFIRM);
            myAgent.send(reply);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
