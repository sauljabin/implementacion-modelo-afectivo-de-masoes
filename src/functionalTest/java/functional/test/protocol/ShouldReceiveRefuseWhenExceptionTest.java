/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol;

import functional.base.FunctionalTest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.protocol.ProtocolRequesterBehaviour;
import test.common.TestException;

public class ShouldReceiveRefuseWhenExceptionTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID agent = createAgent(tester);
        addBehaviour(tester, agent, ExceptionRefuseResponderBehaviour.class.getName());

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(agent);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        ProtocolRequesterBehaviour requesterBehaviour = new ProtocolRequesterBehaviour(null, request) {
            @Override
            protected void handleRefuse(ACLMessage msg) {
                assertEquals("Refuse content", "MESSAGE REFUSE", msg.getContent());
            }

            @Override
            protected void handleNotUnderstood(ACLMessage msg) {
                failed("Not understood");
            }

            @Override
            protected void handleFailure(ACLMessage msg) {
                failed("Failure");
            }

            @Override
            protected void handleInform(ACLMessage msg) {
                failed("Inform");
            }

            @Override
            protected void handleAgree(ACLMessage msg) {
                failed("Agree");
            }
        };

        return requesterBehaviour;
    }

}
