/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol.ontology;

import functional.core.FunctionalTest;
import jade.content.Predicate;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.protocol.OntologyRequesterBehaviour;
import settings.ontology.GetAllSettings;
import settings.ontology.SettingsOntology;
import test.common.TestException;

public class ShouldReceiveNotUnderstoodWhenSendInvalidActionTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID agent = createAgent(tester);
        addBehaviour(tester, agent, NotUnderstoodBehaviourResponderBehaviour.class.getName());

        OntologyRequesterBehaviour requesterBehaviour = new OntologyRequesterBehaviour(null, agent, new GetAllSettings(), new SettingsOntology()) {
            @Override
            protected void handleRefuse(String contentMessage) {
                failed("Refuse");
            }

            @Override
            protected void handleAgree(String contentMessage) {
                failed("Agree");
            }

            @Override
            protected void handleNotUnderstood(String contentMessage) {
                assertEquals("Not understood content", "Unknown ontology settings", contentMessage);
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
