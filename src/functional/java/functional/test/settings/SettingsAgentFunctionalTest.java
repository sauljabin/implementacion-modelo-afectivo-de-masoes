/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.settings;

import application.ApplicationSettings;
import functional.test.FunctionalTest;
import jade.JadeSettings;
import jade.content.ContentElement;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import ontology.settings.GetAllSettings;
import ontology.settings.GetSetting;
import ontology.settings.Setting;
import ontology.settings.SettingsOntology;
import ontology.settings.SystemSettings;
import org.junit.Before;
import org.junit.Test;
import settings.SettingsAgent;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class SettingsAgentFunctionalTest extends FunctionalTest {

    private AID settingsAgentAID;

    @Before
    public void setUp() {
        registerOntology(new SettingsOntology());
        settingsAgentAID = createAgent(SettingsAgent.class);
    }

    @Test
    public void shouldReceiveAllSettings() {
        ContentElement contentElement = sendActionAndWaitContent(settingsAgentAID, new GetAllSettings(), SettingsOntology.ONTOLOGY_NAME);
        assertThat(contentElement, is(instanceOf(SystemSettings.class)));

        SystemSettings systemSettings = (SystemSettings) contentElement;

        SystemSettings expectedSystemSettings = new SystemSettings();

        ApplicationSettings.getInstance().toMap().forEach(
                (key, value) -> expectedSystemSettings.getSettings().add(new Setting(key, value))
        );

        JadeSettings.getInstance().toMap().forEach(
                (key, value) -> expectedSystemSettings.getSettings().add(new Setting(key, value))
        );

        assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReceiveOneSetting() {
        ContentElement contentElement = sendActionAndWaitContent(settingsAgentAID, new GetSetting(ApplicationSettings.APP_NAME), SettingsOntology.ONTOLOGY_NAME);
        assertThat(contentElement, is(instanceOf(SystemSettings.class)));
        SystemSettings systemSettings = (SystemSettings) contentElement;
        SystemSettings expectedSystemSettings = new SystemSettings();
        expectedSystemSettings.getSettings().add(new Setting(ApplicationSettings.APP_NAME, ApplicationSettings.getInstance().get(ApplicationSettings.APP_NAME)));
        assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReceiveFailure() {
        ACLMessage contentMessage = sendActionAndWaitMessage(settingsAgentAID, new GetSetting("no-key"), SettingsOntology.ONTOLOGY_NAME);
        assertEquals("Content", "Setting not found no-key", contentMessage.getContent());
    }

}
