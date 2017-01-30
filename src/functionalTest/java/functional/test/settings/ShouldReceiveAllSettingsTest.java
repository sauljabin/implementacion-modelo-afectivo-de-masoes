/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.settings;

import application.ApplicationSettings;
import functional.test.FunctionalTest;
import jade.JadeSettings;
import jade.content.Predicate;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.util.leap.ArrayList;
import protocol.OntologyRequesterBehaviour;
import settings.SettingsAgent;
import settings.ontology.GetAllSettings;
import settings.ontology.Setting;
import settings.ontology.SettingsOntology;
import settings.ontology.SystemSettings;
import test.common.TestException;

public class ShouldReceiveAllSettingsTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        OntologyRequesterBehaviour requestAllSettings = new OntologyRequesterBehaviour(
                null,
                settingsAgentAID,
                new GetAllSettings(),
                new SettingsOntology()) {

            @Override
            protected void handleInform(Predicate predicate) {
                SystemSettings systemSettings = (SystemSettings) predicate;
                SystemSettings expectedSystemSettings = new SystemSettings(new ArrayList());

                ApplicationSettings.getInstance().toMap().forEach(
                        (key, value) -> expectedSystemSettings.getSettings().add(new Setting(key, value))
                );

                JadeSettings.getInstance().toMap().forEach(
                        (key, value) -> expectedSystemSettings.getSettings().add(new Setting(key, value))
                );

                assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
            }
        };

        return requestAllSettings;
    }

}
