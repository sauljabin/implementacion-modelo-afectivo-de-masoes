/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.behaviour;

import application.settings.ApplicationSettings;
import jade.content.ContentManager;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.language.FipaLanguage;
import jade.logger.JadeLogger;
import jade.settings.JadeSettings;
import jade.settings.ontology.GetAllSettings;
import jade.settings.ontology.GetSetting;
import jade.settings.ontology.Setting;
import jade.settings.ontology.SettingsOntology;
import jade.settings.ontology.SystemSettings;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ResponseSettingsBehaviourTest {

    private Agent mockAgent;
    private ResponseSettingsBehaviour settingsBehaviour;
    private ArgumentCaptor<MessageTemplate> messageTemplateArgumentCaptor;
    private ContentManager contentManager;
    private ACLMessage request;
    private JadeLogger mockLogger;
    private ApplicationSettings mockApplicationSettings;
    private JadeSettings mockJadeSettings;

    @Before
    public void setUp() throws Exception {
        messageTemplateArgumentCaptor = ArgumentCaptor.forClass(MessageTemplate.class);

        settingsBehaviour = new ResponseSettingsBehaviour();
        mockAgent = mock(Agent.class);
        settingsBehaviour.setAgent(mockAgent);

        contentManager = new ContentManager();
        contentManager.registerLanguage(FipaLanguage.getInstance());
        contentManager.registerOntology(SettingsOntology.getInstance());
        when(mockAgent.getContentManager()).thenReturn(contentManager);

        request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(FipaLanguage.LANGUAGE_NAME);
        request.setOntology(SettingsOntology.ONTOLOGY_NAME);

        mockLogger = mock(JadeLogger.class);
        mockApplicationSettings = mock(ApplicationSettings.class);
        mockJadeSettings = mock(JadeSettings.class);
        setFieldValue(settingsBehaviour, "logger", mockLogger);
        setFieldValue(settingsBehaviour, "applicationSettings", mockApplicationSettings);
        setFieldValue(settingsBehaviour, "jadeSettings", mockJadeSettings);
    }

    @Test
    public void shouldSetCorrectMessageTemplate() {
        MessageTemplate expectedTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchLanguage(FipaLanguage.LANGUAGE_NAME));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchOntology(SettingsOntology.ONTOLOGY_NAME));
        ResponseSettingsBehaviour spySettingsBehaviour = spy(new ResponseSettingsBehaviour());
        spySettingsBehaviour.onStart();
        verify(spySettingsBehaviour).setMessageTemplate(messageTemplateArgumentCaptor.capture());
        assertReflectionEquals(expectedTemplate, messageTemplateArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnASetting() throws Exception {
        String keyApp = "keyApp";
        String valueApp = "valueApp";
        when(mockApplicationSettings.get(keyApp)).thenReturn(valueApp);
        testReturnASettings(keyApp, valueApp);
    }

    @Test
    public void shouldReturnAJadeSetting() throws Exception {
        String keyJade = "keyJade";
        String valueJade = "valueJade";
        when(mockJadeSettings.get(keyJade)).thenReturn(valueJade);
        testReturnASettings(keyJade, valueJade);
    }

    @Test
    public void shouldReturnNullSetting() throws Exception {
        String key = "no-key";
        String value = null;
        when(mockApplicationSettings.get(key)).thenReturn(value);
        when(mockJadeSettings.get(key)).thenReturn(value);
        testReturnASettings(key, value);
    }

    private void testReturnASettings(String key, String expectedValue) throws Exception {
        GetSetting getSetting = new GetSetting(key);
        Action action = new Action(new AID(), getSetting);
        contentManager.fillContent(request, action);
        ACLMessage response = settingsBehaviour.prepareResponse(request);
        SystemSettings systemSettings = (SystemSettings) contentManager.extractContent(response);
        Setting expectedSetting = new Setting(key, expectedValue);
        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        assertThat(response.getLanguage(), is(FipaLanguage.LANGUAGE_NAME));
        assertThat(response.getOntology(), is(SettingsOntology.ONTOLOGY_NAME));
        assertReflectionEquals(expectedSetting, systemSettings.getSettings().get(0));
    }

    @Test
    public void shouldReturnAllSettings() throws Exception {
        Map<String, String> appSettingsMap = new HashMap<>();
        appSettingsMap.put("keyApp1", "valueApp1");
        appSettingsMap.put("keyApp2", "valueApp2");
        when(mockApplicationSettings.toMap()).thenReturn(appSettingsMap);

        Map<String, String> jadeSettingsMap = new HashMap<>();
        jadeSettingsMap.put("keyJade1", "valueJade1");
        jadeSettingsMap.put("keyJade2", "valueJade2");
        when(mockJadeSettings.toMap()).thenReturn(jadeSettingsMap);

        SystemSettings expectedSetting = new SystemSettings();
        List appList = new ArrayList();
        expectedSetting.setSettings(appList);

        appSettingsMap.forEach(
                (key, value) -> appList.add(new Setting(key, value))
        );

        jadeSettingsMap.forEach(
                (key, value) -> appList.add(new Setting(key, value))
        );

        Action action = new Action(new AID(), new GetAllSettings());
        contentManager.fillContent(request, action);
        ACLMessage response = settingsBehaviour.prepareResponse(request);
        SystemSettings systemSettings = (SystemSettings) contentManager.extractContent(response);

        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        assertThat(response.getLanguage(), is(FipaLanguage.LANGUAGE_NAME));
        assertThat(response.getOntology(), is(SettingsOntology.ONTOLOGY_NAME));
        assertReflectionEquals(expectedSetting.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldResponseFailureWhenException() throws Exception {
        String message = "message";
        OntologyException ontologyException = new OntologyException(message);

        ContentManager mockContentManager = mock(ContentManager.class);
        when(mockContentManager.extractContent(any())).thenThrow(ontologyException);

        when(mockAgent.getContentManager()).thenReturn(mockContentManager);

        ACLMessage response = settingsBehaviour.prepareResponse(request);

        assertThat(response.getPerformative(), is(ACLMessage.FAILURE));
        assertThat(response.getContent(), is(message));
        verify(mockLogger).agentException(eq(mockAgent), eq(ontologyException));
    }

}