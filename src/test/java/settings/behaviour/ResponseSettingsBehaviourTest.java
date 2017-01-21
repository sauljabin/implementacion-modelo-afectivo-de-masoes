/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings.behaviour;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import settings.application.ApplicationSettings;
import settings.exception.SettingsException;
import settings.jade.JadeSettings;
import settings.ontology.GetAllSettings;
import settings.ontology.GetSetting;
import settings.ontology.Setting;
import settings.ontology.SystemSettings;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ResponseSettingsBehaviourTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Agent agentMock;
    private ResponseSettingsBehaviour settingsBehaviour;
    private ApplicationSettings applicationSettingsMock;
    private JadeSettings jadeSettingsMock;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        applicationSettingsMock = mock(ApplicationSettings.class);
        jadeSettingsMock = mock(JadeSettings.class);

        settingsBehaviour = new ResponseSettingsBehaviour(agentMock);
        setFieldValue(settingsBehaviour, "applicationSettings", applicationSettingsMock);
        setFieldValue(settingsBehaviour, "jadeSettings", jadeSettingsMock);
    }

    @Test
    public void shouldReturnASetting() throws Exception {
        String keyApp = "keyApp";
        String valueApp = "valueApp";
        doReturn(valueApp).when(applicationSettingsMock).get(keyApp);
        testReturnASettings(keyApp, valueApp);
    }

    @Test
    public void shouldReturnAJadeSetting() throws Exception {
        String keyJade = "keyJade";
        String valueJade = "valueJade";
        doReturn(valueJade).when(jadeSettingsMock).get(keyJade);
        testReturnASettings(keyJade, valueJade);
    }

    @Test
    public void shouldThrowExceptionWhenKeyNotFound() throws Exception {
        String key = "no-key";
        expectedException.expectMessage("Setting not found " + key);
        expectedException.expect(SettingsException.class);
        doReturn(null).when(applicationSettingsMock).get(key);
        doReturn(null).when(jadeSettingsMock).get(key);
        GetSetting getSetting = new GetSetting(key);
        Action action = new Action(new AID(), getSetting);
        settingsBehaviour.performAction(action);
    }

    @Test
    public void shouldReturnAllSettings() throws Exception {
        Map<String, String> appSettingsMap = new HashMap<>();
        appSettingsMap.put("keyApp1", "valueApp1");
        appSettingsMap.put("keyApp2", "valueApp2");
        doReturn(appSettingsMap).when(applicationSettingsMock).toMap();

        Map<String, String> jadeSettingsMap = new HashMap<>();
        jadeSettingsMap.put("keyJade1", "valueJade1");
        jadeSettingsMap.put("keyJade2", "valueJade2");
        doReturn(jadeSettingsMap).when(jadeSettingsMock).toMap();

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