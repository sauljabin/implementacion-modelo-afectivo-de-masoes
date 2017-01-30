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
import ontology.ActionResult;
import ontology.BaseOntology;
import ontology.PerformAction;
import test.common.TestException;


public class ShouldReceivePredicateWhenValidActionTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID agent = createAgent(tester);
        addBehaviour(tester, agent, ValidActionResponderBehaviour.class.getName());

        PerformAction agentAction = new PerformAction();
        OntologyRequesterBehaviour requesterBehaviour = new OntologyRequesterBehaviour(null, agent, agentAction, new BaseOntology()) {
            @Override
            protected void handleInform(Predicate predicate) {
                ActionResult expectedResult = new ActionResult("OK", agentAction);
                assertReflectionEquals("Inform content", expectedResult, predicate);
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
                failed("Failure");
            }
        };

        return requesterBehaviour;
    }

}
