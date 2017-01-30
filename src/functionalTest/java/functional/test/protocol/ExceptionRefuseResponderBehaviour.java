/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol;

import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.protocol.ProtocolResponderBehaviour;

public class ExceptionRefuseResponderBehaviour extends ProtocolResponderBehaviour {

    public ExceptionRefuseResponderBehaviour() {
        super(null, MessageTemplate.MatchAll());
    }

    @Override
    protected ACLMessage prepareAcceptanceResponse(ACLMessage request) throws RefuseException {
        throw new RefuseException("MESSAGE REFUSE");
    }

}