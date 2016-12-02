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
        settingsLoader.setSetting(keyForTests, expectedValue);
        assertThat(settingsLoader.getSetting(keyForTests), is(expectedValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        String expectedDefaultValue = "defaultValue";
        assertThat(settingsLoader.getSetting("", expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatKeyIsNull() {
        String expectedDefaultValue = "defaultValue";
        assertThat(settingsLoader.getSetting(null, expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldReturnNullThatKeyIsNull() {
        assertThat(settingsLoader.getSetting(null), is(nullValue()));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        settingsLoader.setSetting(keyForTests, expectedValue);
        assertThat(settingsLoader.getSetting(keyForTests, "anything"), is(expectedValue));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = getInitValues();
        expectedValues.keySet()
                .forEach(key -> assertThat(settingsLoader.getSetting(key), is(expectedValues.get(key))));

        String appNameKey = "app.name",
                appRevisionKey = "app.revision",
                appVersionKey = "app.version";

        assertThat(settingsLoader.getSetting(appNameKey), is(notNullValue()));
        assertThat(settingsLoader.getSetting(appRevisionKey), is(notNullValue()));
        assertThat(settingsLoader.getSetting(appVersionKey), is(notNullValue()));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        assertThat(settingsLoader.toString(), is(settingsLoader.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
        settingsLoader.setSetting(keyForTests, expectedValue);
        assertThat(settingsLoader.getSetting(keyForTests), is(expectedValue));
        settingsLoader.setSetting(keyForTests, null);
        assertThat(settingsLoader.getSetting(keyForTests), is(nullValue()));
    }

    private Map<String, String> getInitValues() {
        String osNameKey = "os.name",
                osArchKey = "os.arch",
                osVersionKey = "os.version",
                javaVersionKey = "java.version",
                javaVendorKey = "java.vendor",
                jadeVersionKey = "jade.version",
                jadeRevisionKey = "jade.revision",
                jadePortKey = "jade.port",
                masoesEnvKey = "masoes.env",
                jadeMtpPortKey = "jade.mtp.port",
                jadeDfAutocleanupKey = "jade.df.autocleanup";

        Map<String, String> initValues = new HashMap<>();
        initValues.put(osNameKey, System.getProperty(osNameKey));
        initValues.put(osArchKey, System.getProperty(osArchKey));
        initValues.put(osVersionKey, System.getProperty(osVersionKey));
        initValues.put(javaVersionKey, System.getProperty(javaVersionKey));
        initValues.put(javaVendorKey, System.getProperty(javaVendorKey));
        initValues.put(jadeVersionKey, jade.core.Runtime.getVersion());
        initValues.put(jadeRevisionKey, jade.core.Runtime.getRevision());
        initValues.put(masoesEnvKey, "dummy");
        initValues.put(jadePortKey, "1099");
        initValues.put(jadeMtpPortKey, "7778");
        initValues.put(jadeDfAutocleanupKey, "true");
        return initValues;
    }

}