/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

public class ProtocolRequestRequesterBehaviour extends SimpleAchieveREInitiator {

    public ProtocolRequestRequesterBehaviour(Agent a, ACLMessage msg) {
        super(a, msg);
    }

}
