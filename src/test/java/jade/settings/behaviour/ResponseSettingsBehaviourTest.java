/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.behaviour;

import application.settings.ApplicationSettings;
import common.settings.SettingsException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ResponseSettingsBehaviourTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Agent mockAgent;
    private ResponseSettingsBehaviour settingsBehaviour;
    private ArgumentCaptor<MessageTemplate> messageTemplateArgumentCaptor;
    private JadeLogger mockLogger;
    private ApplicationSettings mockApplicationSettings;
    private JadeSettings mockJadeSettings;

    @Before
    public void setUp() throws Exception {
        messageTemplateArgumentCaptor = ArgumentCaptor.forClass(MessageTemplate.class);
        mockAgent = mock(Agent.class);
        mockLogger = mock(JadeLogger.class);
        mockApplicationSettings = mock(ApplicationSettings.class);
        mockJadeSettings = mock(JadeSettings.class);

        settingsBehaviour = new ResponseSettingsBehaviour(mockAgent);
        setFieldValue(settingsBehaviour, "logger", mockLogger);
        setFieldValue(settingsBehaviour, "applicationSettings", mockApplicationSettings);
        setFieldValue(settingsBehaviour, "jadeSettings", mockJadeSettings);
    }

    @Test
    public void shouldSetCorrectMessageTemplate() {
        MessageTemplate expectedTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchOntology(SettingsOntology.ONTOLOGY_NAME));
        ResponseSettingsBehaviour spySettingsBehaviour = spy(new ResponseSettingsBehaviour(mockAgent));
        spySettingsBehaviour.onStart();
        verify(spySettingsBehaviour).setMessageTemplate(messageTemplateArgumentCaptor.capture());
        assertReflectionEquals(expectedTemplate, messageTemplateArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnASetting() throws Exception {
        String keyApp = "keyApp";
        String valueApp = "valueApp";
        doReturn(valueApp).when(mockApplicationSettings).get(keyApp);
        testReturnASettings(keyApp, valueApp);
    }

    @Test
    public void shouldReturnAJadeSetting() throws Exception {
        String keyJade = "keyJade";
        String valueJade = "valueJade";
        doReturn(valueJade).when(mockJadeSettings).get(keyJade);
        testReturnASettings(keyJade, valueJade);
    }

    @Test
    public void shouldThrowExceptionWhenKeyNotFound() throws Exception {
        String key = "no-key";
        expectedException.expectMessage("Setting not found " + key);
        expectedException.expect(SettingsException.class);
        doReturn(null).when(mockApplicationSettings).get(key);
        doReturn(null).when(mockJadeSettings).get(key);
        GetSetting getSetting = new GetSetting(key);
        Action action = new Action(new AID(), getSetting);
        settingsBehaviour.performAction(action);
    }

    @Test
    public void shouldReturnAllSettings() throws Exception {
        Map<String, String> appSettingsMap = new HashMap<>();
        appSettingsMap.put("keyApp1", "valueApp1");
        appSettingsMap.put("keyApp2", "valueApp2");
        doReturn(appSettingsMap).when(mockApplicationSettings).toMap();

        Map<String, String> jadeSettingsMap = new HashMap<>();
        jadeSettingsMap.put("keyJade1", "valueJade1");
        jadeSettingsMap.put("keyJade2", "valueJade2");
        doReturn(jadeSettingsMap).when(mockJadeSettings).toMap();

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
        SystemSettings systemSettings = (SystemSettings) settingsBehaviour.performAction(action);
        assertReflectionEquals(expectedSetting.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action actionGetSetting = new Action(new AID(), new GetSetting());
        Action actionGetAllSettings = new Action(new AID(), new GetAllSettings());
        assertTrue(settingsBehaviour.isValidAction(actionGetSetting));
        assertTrue(settingsBehaviour.isValidAction(actionGetAllSettings));
    }

    private void testReturnASettings(String key, String expectedValue) throws Exception {
        GetSetting getSetting = new GetSetting(key);
        Action action = new Action(new AID(), getSetting);
        SystemSettings systemSettings = (SystemSettings) settingsBehaviour.performAction(action);
        Setting expectedSetting = new Setting(key, expectedValue);
        assertReflectionEquals(expectedSetting, systemSettings.getSettings().get(0));
    }

}