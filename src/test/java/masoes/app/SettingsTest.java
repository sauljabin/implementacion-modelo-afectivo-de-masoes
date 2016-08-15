/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class SettingsTest {

    private Settings settings;
    private String key;
    private String value;
    private String defaultValue;
    private String noKey;
    private String osNameKey;
    private String osArchKey;
    private String osVersionKey;
    private String javaVersionKey;
    private String javaVendorKey;
    private String keyInFile;
    private String valueInFile;

    @Before
    public void setUp() throws Exception {
        settings = Settings.getInstance();
        key = "key";
        value = "value";
        keyInFile = "keyInFile";
        valueInFile = "valueInFile";
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
        Settings expectedSettings = Settings.getInstance();
        assertTrue(expectedSettings == settings);
    }

    @Test
    public void shouldGetCorrectSetting() {
        settings.set(key, value);
        assertEquals(value, settings.get(key));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        assertEquals(defaultValue, settings.get(noKey, defaultValue));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        settings.set(key, value);
        assertEquals(value, settings.get(key, defaultValue));
    }

    @Test
    public void shouldClearSettingsWhenInvokeClear() {
        settings.set(key, value);
        settings.clear();
        assertNull(settings.get(key));
    }

    @Test
    public void shouldLoadInitValuesWhenInvokeInit() {
        Map<String, String> expectedValues = getInitValues();
        Set<String> keys = expectedValues.keySet();
        settings.init();
        for (String key : keys) {
            assertEquals(expectedValues.get(key), settings.get(key));
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
        settings.clear();
        settings.set(key, value);
        Map<String, String> actualMap = settings.toMap();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void shouldLoadPropertiesFromFile() throws IOException {
        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put(keyInFile, valueInFile);
        settings.clear();
        settings.load();
        Map<String, String> actualMap = settings.toMap();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void shouldToStringSameStringThatMap() {
        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put(key, value);
        String expectedString = expectedMap.toString();
        settings.clear();
        settings.set(key, value);
        assertEquals(expectedString, settings.toString());
    }

}