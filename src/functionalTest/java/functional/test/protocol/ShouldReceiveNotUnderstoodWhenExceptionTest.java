/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol;

import functional.test.core.FunctionalTest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.protocol.ProtocolRequesterBehaviour;
import test.common.TestException;

public class ShouldReceiveNotUnderstoodWhenExceptionTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID agent = createAgent(tester);
        addBehaviour(tester, agent, ExceptionNotUnderstoodResponderBehaviour.class.getName());

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(agent);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        ProtocolRequesterBehaviour requesterBehaviour = new ProtocolRequesterBehaviour(null, request) {
            @Override
            protected void handleNotUnderstood(ACLMessage msg) {
                assertEquals("Not Understood content", msg.getContent(), "MESSAGE NOT UNDERSTOOD");
            }
        };

        return requesterBehaviour;
    }

}
