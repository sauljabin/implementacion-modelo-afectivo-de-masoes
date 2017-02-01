/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.ontology;

import functional.test.FunctionalTest;
import jade.content.Predicate;
import jade.content.onto.BasicOntology;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import protocol.OntologyRequesterBehaviour;
import test.common.TestException;


public class ShouldReceivePredicateWhenValidActionTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID agent = createAgent(tester);
        addBehaviour(tester, agent, ValidActionResponderBehaviour.class.getName());

        OntologyRequesterBehaviour requesterBehaviour = new OntologyRequesterBehaviour(null, agent, new Action(), BasicOntology.getInstance()) {
            @Override
            protected void handleInform(Predicate predicate) {
                assertReflectionEquals("Inform content", new Done(), predicate);
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
