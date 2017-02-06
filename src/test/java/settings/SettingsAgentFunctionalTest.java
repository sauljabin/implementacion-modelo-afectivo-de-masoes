/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import application.ApplicationSettings;
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
import test.FunctionalTest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class SettingsAgentFunctionalTest extends FunctionalTest {

    private AID settingsAgentAID;

    @Before
    public void setUp() {
        registerOntology(SettingsOntology.getInstance());
        settingsAgentAID = createAgent(SettingsAgent.class);
    }

    @Test
    public void shouldReceiveAllSettings() {
        ContentElement contentElement = sendActionAndWaitContent(settingsAgentAID, SettingsOntology.getInstance(), new GetAllSettings());
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
        ContentElement contentElement = sendActionAndWaitContent(settingsAgentAID, SettingsOntology.getInstance(), new GetSetting(ApplicationSettings.APP_NAME));
        assertThat(contentElement, is(instanceOf(SystemSettings.class)));
        SystemSettings systemSettings = (SystemSettings) contentElement;
        SystemSettings expectedSystemSettings = new SystemSettings();
        expectedSystemSettings.getSettings().add(new Setting(ApplicationSettings.APP_NAME, ApplicationSettings.getInstance().get(ApplicationSettings.APP_NAME)));
        assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReceiveFailure() {
        ACLMessage contentMessage = sendActionAndWaitMessage(settingsAgentAID, SettingsOntology.getInstance(), new GetSetting("no-key"));
        assertEquals("Content", "Setting not found no-key", contentMessage.getContent());
    }

}
