/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol.ontology;

import functional.test.core.FunctionalTest;
import jade.content.Predicate;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.ontology.base.BaseOntology;
import jade.ontology.base.PerformAction;
import jade.protocol.OntologyRequesterBehaviour;
import test.common.TestException;

public class ShouldReceiveRefuseWhenSendInvalidActionTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID agent = createAgent(tester);
        addBehaviour(tester, agent, InvalidActionResponderBehaviour.class.getName());

        OntologyRequesterBehaviour requesterBehaviour = new OntologyRequesterBehaviour(null, agent, new PerformAction(), new BaseOntology()) {
            @Override
            protected void handleRefuse(String contentMessage) {
                assertEquals("Refuse content", "Action no valid", contentMessage);
            }

            @Override
            protected void handleAgree(String contentMessage) {
                failed("Agree");
            }

            @Override
            protected void handleNotUnderstood(String contentMessage) {
                failed("Not understood");
            }

            @Override
            protected void handleFailure(String contentMessage) {
                failed("Failure");
            }

            @Override
            protected void handleInform(Predicate predicate) {
                failed("Inform");
            }
        };

        return requesterBehaviour;
    }

}
