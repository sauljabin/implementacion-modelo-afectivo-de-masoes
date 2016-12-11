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
import static org.junit.Assert.assertThat;

public class SettingsLoaderTest {

    private SettingsLoader settingsLoader;
    private String keyForTests;
    private String expectedValue;

    @Before
    public void setUp() {
        settingsLoader = SettingsLoader.getInstance();
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
        settingsLoader.set(keyForTests, expectedValue);
        assertThat(settingsLoader.get(keyForTests), is(expectedValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        String expectedDefaultValue = "defaultValue";
        assertThat(settingsLoader.get("", expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatKeyIsNull() {
        String expectedDefaultValue = "defaultValue";
        assertThat(settingsLoader.get(null, expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldReturnNullThatKeyIsNull() {
        assertThat(settingsLoader.get(null), is(nullValue()));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        settingsLoader.set(keyForTests, expectedValue);
        assertThat(settingsLoader.get(keyForTests, "anything"), is(expectedValue));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = getInitValues();
        expectedValues.keySet()
                .forEach(key -> assertThat(settingsLoader.get(key), is(expectedValues.get(key))));

        String appNameKey = "app.name",
                appRevisionKey = "app.revision",
                appVersionKey = "app.version";

        assertThat(settingsLoader.get(appNameKey), is(notNullValue()));
        assertThat(settingsLoader.get(appRevisionKey), is(notNullValue()));
        assertThat(settingsLoader.get(appVersionKey), is(notNullValue()));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        assertThat(settingsLoader.toString(), is(settingsLoader.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
        settingsLoader.set(keyForTests, expectedValue);
        assertThat(settingsLoader.get(keyForTests), is(expectedValue));
        settingsLoader.set(keyForTests, null);
        assertThat(settingsLoader.get(keyForTests), is(nullValue()));
    }

    private Map<String, String> getInitValues() {
        String osNameKey = "os.name",
                osArchKey = "os.arch",
                osVersionKey = "os.version",
                javaVersionKey = "java.version",
                javaVendorKey = "java.vendor",
                jadeVersionKey = "jade.version",
                jadeRevisionKey = "jade.revision",
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
        return initValues;
    }

}