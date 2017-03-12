/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import application.ApplicationSettings;
import jade.JadeSettings;
import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import ontology.OntologyAssistant;
import ontology.settings.GetAllSettings;
import ontology.settings.GetSetting;
import ontology.settings.Setting;
import ontology.settings.SettingsOntology;
import ontology.settings.SystemSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.ProtocolAssistant;
import test.FunctionalTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class SettingsAgentFunctionalTest extends FunctionalTest {

    private AID settingsAgentAID;
    private OntologyAssistant ontologyAssistant;
    private ProtocolAssistant protocolAssistant;

    @Before
    public void setUp() {
        settingsAgentAID = createAgent(SettingsAgent.class, null);
        ontologyAssistant = createOntologyAssistant(SettingsOntology.getInstance());
        protocolAssistant = createProtocolAssistant();
    }

    @After
    public void tearDown() {
        killAgent(settingsAgentAID);
    }

    @Test
    public void shouldGetAllServicesFromDF() {
        List<ServiceDescription> services = services(settingsAgentAID);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(SettingsOntology.ACTION_GET_ALL_SETTINGS, SettingsOntology.ACTION_GET_SETTING));
    }

    @Test
    public void shouldReceiveAllSettings() {

        GetAllSettings action = new GetAllSettings();
        ContentElement contentElement = sendAction(action);

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

    private ContentElement sendAction(AgentAction action) {
        ACLMessage requestAction = ontologyAssistant.createRequestAction(settingsAgentAID, action);
        ACLMessage message = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);
        return ontologyAssistant.extractMessageContent(message);
    }

    @Test
    public void shouldReceiveOneSetting() {
        ContentElement contentElement = sendAction(new GetSetting(ApplicationSettings.APP_NAME));
        assertThat(contentElement, is(instanceOf(SystemSettings.class)));
        SystemSettings systemSettings = (SystemSettings) contentElement;
        SystemSettings expectedSystemSettings = new SystemSettings();
        expectedSystemSettings.getSettings().add(new Setting(ApplicationSettings.APP_NAME, ApplicationSettings.getInstance().get(ApplicationSettings.APP_NAME)));
        assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReceiveFailure() {
        GetSetting action = new GetSetting("no-key");
        ACLMessage requestAction = ontologyAssistant.createRequestAction(settingsAgentAID, action);
        ACLMessage response = protocolAssistant.sendRequest(requestAction, ACLMessage.FAILURE);
        assertEquals("Content", "Setting not found no-key", response.getContent());
    }

}
