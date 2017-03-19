/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import application.ApplicationSettings;
import jade.JadeSettings;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import masoes.MasoesSettings;
import ontology.OntologyMatchExpression;
import ontology.settings.GetAllSettings;
import ontology.settings.GetSetting;
import ontology.settings.Setting;
import ontology.settings.SettingsOntology;
import ontology.settings.SystemSettings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ResponseSettingsBehaviourTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Agent agentMock;
    private ResponseSettingsBehaviour responseSettingsBehaviour;
    private ApplicationSettings applicationSettingsMock;
    private JadeSettings jadeSettingsMock;
    private MasoesSettings masoesSettingsMock;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        applicationSettingsMock = mock(ApplicationSettings.class);
        jadeSettingsMock = mock(JadeSettings.class);
        masoesSettingsMock = mock(MasoesSettings.class);

        responseSettingsBehaviour = new ResponseSettingsBehaviour(agentMock);
        setFieldValue(responseSettingsBehaviour, "applicationSettings", applicationSettingsMock);
        setFieldValue(responseSettingsBehaviour, "jadeSettings", jadeSettingsMock);
        setFieldValue(responseSettingsBehaviour, "masoesSettings", masoesSettingsMock);
    }

    @Test
    public void shouldReturnASetting() {
        String keyApp = "keyApp";
        String valueApp = "valueApp";
        doReturn(valueApp).when(applicationSettingsMock).get(keyApp);
        testReturnASettings(keyApp, valueApp);
    }

    @Test
    public void shouldReturnAJadeSetting() {
        String keyJade = "keyJade";
        String valueJade = "valueJade";
        doReturn(valueJade).when(jadeSettingsMock).get(keyJade);
        testReturnASettings(keyJade, valueJade);
    }

    @Test
    public void shouldReturnAMasoesSetting() {
        String keyMasoes = "keyMasoes";
        String valueMasoes = "valueMasoes";
        doReturn(valueMasoes).when(masoesSettingsMock).get(keyMasoes);
        testReturnASettings(keyMasoes, valueMasoes);
    }

    @Test
    public void shouldThrowExceptionWhenKeyNotFound() {
        String key = "no-key";
        expectedException.expectMessage("Setting not found " + key);
        expectedException.expect(SettingsException.class);
        doReturn(null).when(applicationSettingsMock).get(key);
        doReturn(null).when(jadeSettingsMock).get(key);
        doReturn(null).when(masoesSettingsMock).get(key);
        GetSetting getSetting = new GetSetting(key);
        Action action = new Action(new AID(), getSetting);
        responseSettingsBehaviour.performAction(action);
    }

    @Test
    public void shouldReturnAllSettings() {
        Map<String, String> appSettingsMap = new HashMap<>();
        appSettingsMap.put("keyApp1", "valueApp1");
        appSettingsMap.put("keyApp2", "valueApp2");
        doReturn(appSettingsMap).when(applicationSettingsMock).toMap();

        Map<String, String> jadeSettingsMap = new HashMap<>();
        jadeSettingsMap.put("keyJade1", "valueJade1");
        jadeSettingsMap.put("keyJade2", "valueJade2");
        doReturn(jadeSettingsMap).when(jadeSettingsMock).toMap();

        Map<String, String> masoesSettingsMap = new HashMap<>();
        masoesSettingsMap.put("keyMasoes1", "valueMasoes1");
        masoesSettingsMap.put("keyMasoes2", "valueMasoes");
        doReturn(masoesSettingsMap).when(masoesSettingsMock).toMap();

        SystemSettings expectedSetting = new SystemSettings();
        List appList = new ArrayList();
        expectedSetting.setSettings(appList);

        appSettingsMap.forEach(
                (key, value) -> appList.add(new Setting(key, value))
        );

        jadeSettingsMap.forEach(
                (key, value) -> appList.add(new Setting(key, value))
        );

        masoesSettingsMap.forEach(
                (key, value) -> appList.add(new Setting(key, value))
        );

        Action action = new Action(new AID(), new GetAllSettings());
        SystemSettings systemSettings = (SystemSettings) responseSettingsBehaviour.performAction(action);
        assertReflectionEquals(expectedSetting.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action actionGetSetting = new Action(new AID(), new GetSetting());
        Action actionGetAllSettings = new Action(new AID(), new GetAllSettings());
        assertTrue(responseSettingsBehaviour.isValidAction(actionGetSetting));
        assertTrue(responseSettingsBehaviour.isValidAction(actionGetAllSettings));
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(responseSettingsBehaviour.getOntology(), is(instanceOf(SettingsOntology.class)));
        assertReflectionEquals(new MessageTemplate(new OntologyMatchExpression(SettingsOntology.getInstance())), responseSettingsBehaviour.getMessageTemplate());
    }

    private void testReturnASettings(String key, String expectedValue) {
        GetSetting getSetting = new GetSetting(key);
        Action action = new Action(new AID(), getSetting);
        SystemSettings systemSettings = (SystemSettings) responseSettingsBehaviour.performAction(action);
        Setting expectedSetting = new Setting(key, expectedValue);
        assertReflectionEquals(expectedSetting, systemSettings.getSettings().get(0));
    }

}