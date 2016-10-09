/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class SettingsLoaderTest {

    private SettingsLoader settingsLoader;
    private String keyForTests;
    private String expectedValue;

    @Before
    public void setUp() throws Exception {
        settingsLoader = SettingsLoader.getInstance();
        keyForTests = "keyForTests";
        expectedValue = "expectedValue";
    }

    @Test
    public void shouldGetSameInstance() {
        SettingsLoader expectedSettingsLoader = SettingsLoader.getInstance();
        assertThat(settingsLoader, is(expectedSettingsLoader));
    }

    @Test
    public void shouldGetCorrectSetting() {
        settingsLoader.setSetting(keyForTests, expectedValue);
        assertEquals(expectedValue, settingsLoader.getSetting(keyForTests));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        String expectedDefaultValue = "defaultValue";
        assertEquals(expectedDefaultValue, settingsLoader.getSetting("", expectedDefaultValue));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        settingsLoader.setSetting(keyForTests, expectedValue);
        assertEquals(expectedValue, settingsLoader.getSetting(keyForTests, "anything"));
    }

    @Test
    public void shouldClearSettingsWhenInvokeClear() {
        settingsLoader.setSetting(keyForTests, expectedValue);
        settingsLoader.clear();
        assertNull(settingsLoader.getSetting(keyForTests));
    }

    @Test
    public void shouldLoadInitValuesWhenInvokeInit() {
        Map<String, String> expectedValues = getInitValues();
        settingsLoader.init();
        expectedValues.keySet()
                .forEach(key -> assertEquals(expectedValues.get(key), settingsLoader.getSetting(key)));
    }

    @Test
    public void shouldGetACorrectMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(keyForTests, expectedValue);
        settingsLoader.clear();
        settingsLoader.setSetting(keyForTests, expectedValue);
        Map<String, String> actualMap = settingsLoader.toMap();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void shouldLoadPropertiesFromFile() throws IOException {
        String appNameKey = "app.name",
                appRevisionKey = "app.revision",
                appVersionKey = "app.version",
                masoesEnvKey = "masoes.env";

        List<String> keys = new ArrayList<>();
        keys.add(appNameKey);
        keys.add(appRevisionKey);
        keys.add(appVersionKey);
        keys.add(masoesEnvKey);
        settingsLoader.clear();
        settingsLoader.loadFromFile();
        List<String> expected = settingsLoader.getKeys();
        Collections.sort(keys);
        assertReflectionEquals(expected, keys);
    }

    @Test
    public void shouldToStringSameStringThatMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(keyForTests, expectedValue);
        String expectedString = expectedMap.toString();
        settingsLoader.clear();
        settingsLoader.setSetting(keyForTests, expectedValue);
        assertEquals(expectedString, settingsLoader.toString());
    }

    public Map<String, String> getInitValues() {
        String osNameKey = "os.name",
                osArchKey = "os.arch",
                osVersionKey = "os.version",
                javaVersionKey = "java.version",
                javaVendorKey = "java.vendor",
                jadeVersionKey = "jade.version",
                jadeRevisionKey = "jade.revision";

        Map<String, String> initValues = new HashMap<>();
        initValues.put(osNameKey, System.getProperty(osNameKey));
        initValues.put(osArchKey, System.getProperty(osArchKey));
        initValues.put(osVersionKey, System.getProperty(osVersionKey));
        initValues.put(javaVersionKey, System.getProperty(javaVersionKey));
        initValues.put(javaVendorKey, System.getProperty(javaVendorKey));
        initValues.put(jadeVersionKey, jade.core.Runtime.getVersion());
        initValues.put(jadeRevisionKey, jade.core.Runtime.getRevision());
        return initValues;
    }

}