/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class ProtocolRequestResponderBehaviour extends SimpleAchieveREResponder {

    public ProtocolRequestResponderBehaviour() {
        super(null, MessageTemplate.MatchAll());
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        reset(messageTemplate);
    }

}
