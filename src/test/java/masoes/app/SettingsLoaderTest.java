/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.junit.Before;
import org.junit.Test;

import masoes.app.SettingsLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class SettingsLoaderTest {

    private SettingsLoader settingsLoader;
    private String key;
    private String value;
    private String defaultValue;
    private String noKey;
    private String osNameKey;
    private String osArchKey;
    private String osVersionKey;
    private String javaVersionKey;
    private String javaVendorKey;

    @Before
    public void setUp() throws Exception {
        settingsLoader = SettingsLoader.getInstance();
        key = "key";
        value = "value";
        defaultValue = "defaultValue";
        noKey = "noKey";
        osNameKey = "os.name";
        osArchKey = "os.arch";
        osVersionKey = "os.version";
        javaVersionKey = "java.version";
        javaVendorKey = "java.vendor";
    }

    @Test
    public void shouldGetSameInstance() {
        SettingsLoader expectedSettingsLoader = SettingsLoader.getInstance();
        assertTrue(expectedSettingsLoader == settingsLoader);
    }

    @Test
    public void shouldGetCorrectSetting() {
        settingsLoader.set(key, value);
        assertEquals(value, settingsLoader.get(key));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        assertEquals(defaultValue, settingsLoader.get(noKey, defaultValue));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        settingsLoader.set(key, value);
        assertEquals(value, settingsLoader.get(key, defaultValue));
    }

    @Test
    public void shouldClearSettingsWhenInvokeClear() {
        settingsLoader.set(key, value);
        settingsLoader.clear();
        assertNull(settingsLoader.get(key));
    }

    @Test
    public void shouldLoadInitValuesWhenInvokeInit() {
        Map<String, String> expectedValues = getInitValues();
        Set<String> keys = expectedValues.keySet();
        settingsLoader.init();
        for (String key : keys) {
            assertEquals(expectedValues.get(key), settingsLoader.get(key));
        }
    }

    public Map<String, String> getInitValues() {
        Map<String, String> initValues = new HashMap<String, String>();
        initValues.put(osNameKey, System.getProperty(osNameKey));
        initValues.put(osArchKey, System.getProperty(osArchKey));
        initValues.put(osVersionKey, System.getProperty(osVersionKey));
        initValues.put(javaVersionKey, System.getProperty(javaVersionKey));
        initValues.put(javaVendorKey, System.getProperty(javaVendorKey));
        return initValues;
    }

    @Test
    public void shouldGetACorrectMap() {
        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put(key, value);
        settingsLoader.clear();
        settingsLoader.set(key, value);
        Map<String, String> actualMap = settingsLoader.toMap();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void shouldLoadPropertiesFromFile() throws IOException {
        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("app.name", "appName");
        expectedMap.put("app.revision", "1");
        expectedMap.put("app.version", "1");
        settingsLoader.clear();
        settingsLoader.load();
        Map<String, String> actualMap = settingsLoader.toMap();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void shouldToStringSameStringThatMap() {
        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put(key, value);
        String expectedString = expectedMap.toString();
        settingsLoader.clear();
        settingsLoader.set(key, value);
        assertEquals(expectedString, settingsLoader.toString());
    }

}