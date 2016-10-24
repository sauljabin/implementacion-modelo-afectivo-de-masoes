/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SettingsLoaderTest {

    private SettingsLoader settingsLoader = SettingsLoader.getInstance();
    private String keyForTests;
    private String expectedValue;

    @Before
    public void setUp() throws Exception {
        settingsLoader.load();
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
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = getInitValues();
        expectedValues.keySet()
                .forEach(key -> assertEquals(expectedValues.get(key), settingsLoader.getSetting(key)));

        String appNameKey = "app.name",
                appRevisionKey = "app.revision",
                appVersionKey = "app.version";

        assertThat(settingsLoader.getSetting(appNameKey), is(notNullValue()));
        assertThat(settingsLoader.getSetting(appRevisionKey), is(notNullValue()));
        assertThat(settingsLoader.getSetting(appVersionKey), is(notNullValue()));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        assertEquals(settingsLoader.toMap().toString(), settingsLoader.toString());
    }

    @Test
    public void shouldRemoveProperty() {
        settingsLoader.setSetting(keyForTests, expectedValue);
        assertThat(settingsLoader.getSetting(keyForTests), is(expectedValue));
        settingsLoader.setSetting(keyForTests, null);
        assertThat(settingsLoader.getSetting(keyForTests), is(nullValue()));
    }

    public Map<String, String> getInitValues() {
        String osNameKey = "os.name",
                osArchKey = "os.arch",
                osVersionKey = "os.version",
                javaVersionKey = "java.version",
                javaVendorKey = "java.vendor",
                jadeVersionKey = "jade.version",
                jadeRevisionKey = "jade.revision",
                jadePortKey = "jade.port",
                masoesEnvKey = "masoes.env";

        Map<String, String> initValues = new HashMap<>();
        initValues.put(osNameKey, System.getProperty(osNameKey));
        initValues.put(osArchKey, System.getProperty(osArchKey));
        initValues.put(osVersionKey, System.getProperty(osVersionKey));
        initValues.put(javaVersionKey, System.getProperty(javaVersionKey));
        initValues.put(javaVendorKey, System.getProperty(javaVendorKey));
        initValues.put(jadeVersionKey, jade.core.Runtime.getVersion());
        initValues.put(jadeRevisionKey, jade.core.Runtime.getRevision());
        initValues.put(masoesEnvKey, "dummy");
        initValues.put(jadePortKey, "2000");
        return initValues;
    }

}