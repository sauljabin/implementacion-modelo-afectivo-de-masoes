/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.settings;

import functional.base.FunctionalTest;
import jade.content.Predicate;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.protocol.OntologyRequesterBehaviour;
import jade.util.leap.ArrayList;
import settings.agent.SettingsAgent;
import settings.loader.ApplicationSettings;
import settings.ontology.GetSetting;
import settings.ontology.Setting;
import settings.ontology.SettingsOntology;
import settings.ontology.SystemSettings;
import test.common.TestException;

public class ShouldReceiveASettingTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        OntologyRequesterBehaviour requestASetting = new OntologyRequesterBehaviour(
                null,
                settingsAgentAID,
                new GetSetting(ApplicationSettings.APP_NAME),
                new SettingsOntology()) {

            @Override
            protected void handleInform(Predicate predicate) {
                SystemSettings systemSettings = (SystemSettings) predicate;
                SystemSettings expectedSystemSettings = new SystemSettings(new ArrayList());

                expectedSystemSettings.getSettings().add(new Setting(ApplicationSettings.APP_NAME, ApplicationSettings.getInstance().get(ApplicationSettings.APP_NAME)));

                assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
            }
        };

        return requestASetting;
    }

}
