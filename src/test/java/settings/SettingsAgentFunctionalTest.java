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
import masoes.MasoesSettings;
import ontology.OntologyAssistant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.ProtocolAssistant;
import settings.ontology.GetAllSettings;
import settings.ontology.GetSetting;
import settings.ontology.Setting;
import settings.ontology.SettingsOntology;
import settings.ontology.SystemSettings;
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

        MasoesSettings.getInstance().toMap().forEach(
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
    public void shouldReceiveOneApplicationSetting() {
        ContentElement contentElement = sendAction(new GetSetting(ApplicationSettings.APP_NAME));
        assertThat(contentElement, is(instanceOf(SystemSettings.class)));

        SystemSettings expectedSystemSettings = new SystemSettings();
        expectedSystemSettings.getSettings().add(new Setting(ApplicationSettings.APP_NAME, ApplicationSettings.getInstance().get(ApplicationSettings.APP_NAME)));

        SystemSettings systemSettings = (SystemSettings) contentElement;
        assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReceiveOneMasoesSetting() {
        ContentElement contentElement = sendAction(new GetSetting(MasoesSettings.MASOES_ACTIVATION_INCREASE));
        assertThat(contentElement, is(instanceOf(SystemSettings.class)));

        SystemSettings expectedSystemSettings = new SystemSettings();
        expectedSystemSettings.getSettings().add(new Setting(MasoesSettings.MASOES_ACTIVATION_INCREASE, MasoesSettings.getInstance().get(MasoesSettings.MASOES_ACTIVATION_INCREASE)));

        SystemSettings systemSettings = (SystemSettings) contentElement;
        assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReceiveOneJadeSetting() {
        ContentElement contentElement = sendAction(new GetSetting(JadeSettings.JADE_DOMAIN_DF_AUTOCLEANUP));
        assertThat(contentElement, is(instanceOf(SystemSettings.class)));

        SystemSettings expectedSystemSettings = new SystemSettings();
        expectedSystemSettings.getSettings().add(new Setting(JadeSettings.JADE_DOMAIN_DF_AUTOCLEANUP, JadeSettings.getInstance().get(JadeSettings.JADE_DOMAIN_DF_AUTOCLEANUP)));

        SystemSettings systemSettings = (SystemSettings) contentElement;
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
