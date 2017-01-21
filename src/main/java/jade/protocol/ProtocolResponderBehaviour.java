/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class ProtocolResponderBehaviour extends SimpleAchieveREResponder {

    public ProtocolResponderBehaviour(Agent agent) {
        super(agent, MessageTemplate.MatchAll());
    }

    public ProtocolResponderBehaviour(Agent agent, MessageTemplate messageTemplate) {
        super(agent, messageTemplate);
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        reset(messageTemplate);
    }

}
