/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class GuiResponseHandlerBehaviour extends Behaviour {

    private RequesterGui requesterGui;

    public GuiResponseHandlerBehaviour(RequesterGuiAgent requesterGuiAgent, RequesterGui requesterGui) {
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
