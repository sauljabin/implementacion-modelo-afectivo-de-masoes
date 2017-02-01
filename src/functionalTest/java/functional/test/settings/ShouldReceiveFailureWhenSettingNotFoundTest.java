/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.settings;

import functional.test.FunctionalTest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import ontology.settings.GetSetting;
import ontology.settings.SettingsOntology;
import protocol.OntologyRequesterBehaviour;
import settings.SettingsAgent;
import test.common.TestException;

public class ShouldReceiveFailureWhenSettingNotFoundTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        OntologyRequesterBehaviour requestASetting = new OntologyRequesterBehaviour(
                null,
                settingsAgentAID,
                new GetSetting("no-key"),
                new SettingsOntology()) {

            @Override
            protected void handleFailure(String contentMessage) {
                assertEquals("Content", "Setting not found no-key", contentMessage);
            }
        };

        return requestASetting;
    }

}
