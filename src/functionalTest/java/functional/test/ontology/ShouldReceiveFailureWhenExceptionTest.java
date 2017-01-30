/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.ontology;

import functional.test.FunctionalTest;
import jade.content.Predicate;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.protocol.OntologyRequesterBehaviour;
import ontology.BaseOntology;
import ontology.PerformAction;
import test.common.TestException;

public class ShouldReceiveFailureWhenExceptionTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID agent = createAgent(tester);
        addBehaviour(tester, agent, ExceptionFailureResponderBehaviour.class.getName());

        PerformAction agentAction = new PerformAction();
        OntologyRequesterBehaviour requesterBehaviour = new OntologyRequesterBehaviour(null, agent, agentAction, new BaseOntology()) {
            @Override
            protected void handleInform(Predicate predicate) {
                failed("Inform");
            }

            @Override
            protected void handleRefuse(String contentMessage) {
                failed("Refuse");
            }

            @Override
            protected void handleNotUnderstood(String contentMessage) {
                failed("Not understood");
            }

            @Override
            protected void handleFailure(String contentMessage) {
                assertEquals("Failure content", "MESSAGE FAILURE", contentMessage);
            }
        };

        return requesterBehaviour;
    }

}
