/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class RequesterGuiResponseHandlerBehaviour extends Behaviour {

    private RequesterGui requesterGui;

    public RequesterGuiResponseHandlerBehaviour(RequesterGuiAgent requesterGuiAgent, RequesterGui requesterGui) {
        super(requesterGuiAgent);
        this.requesterGui = requesterGui;
    }

    @Override
    public void action() {
        ACLMessage receive = myAgent.receive();
        if (receive != null) {
            requesterGui.logMessage(receive);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
