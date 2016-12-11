/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.settings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ApplicationSettingsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ApplicationSettings applicationSettings;
    private String key;
    private String expectedValue;

    @Before
    public void setUp() {
        applicationSettings = ApplicationSettings.getInstance();
        key = "key";
        expectedValue = "expectedValue";
    }

    @Test
    public void shouldGetSameInstance() {
        ApplicationSettings expectedApplicationSettings = ApplicationSettings.getInstance();
        assertThat(applicationSettings, is(expectedApplicationSettings));
    }

    @Test
    public void shouldThrowsExceptionInSetWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        applicationSettings.set(key, expectedValue);
    }

    @Test
    public void shouldThrowsExceptionInGetWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        applicationSettings.get(key);
    }

    @Test
    public void shouldThrowsExceptionInGetDefaultWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        applicationSettings.get(key, "default");
    }

    @Test
    public void shouldThrowsExceptionInToMapWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        applicationSettings.toMap();
    }

    @Test
    public void shouldThrowsExceptionInToStringWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        applicationSettings.toString();
    }

    @Test
    public void shouldGetCorrectSetting() {
        applicationSettings.load();
        applicationSettings.set(key, expectedValue);
        assertThat(applicationSettings.get(key), is(expectedValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        applicationSettings.load();
        String expectedDefaultValue = "defaultValue";
        assertThat(applicationSettings.get("", expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatKeyIsNull() {
        applicationSettings.load();
        String expectedDefaultValue = "defaultValue";
        assertThat(applicationSettings.get(null, expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldReturnNullThatKeyIsNull() {
        applicationSettings.load();
        assertThat(applicationSettings.get(null), is(nullValue()));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        applicationSettings.load();
        applicationSettings.set(key, expectedValue);
        assertThat(applicationSettings.get(key, "anything"), is(expectedValue));
    }

    @Test
    public void shouldLoadInitValues() {
        applicationSettings.load();
        Map<String, String> expectedValues = getInitValues();
        expectedValues.keySet()
                .forEach(key -> assertThat(applicationSettings.get(key), is(expectedValues.get(key))));

        String appNameKey = "app.name",
                appRevisionKey = "app.revision",
                appVersionKey = "app.version";

        assertThat(applicationSettings.get(appNameKey), is(notNullValue()));
        assertThat(applicationSettings.get(appRevisionKey), is(notNullValue()));
        assertThat(applicationSettings.get(appVersionKey), is(notNullValue()));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        applicationSettings.load();
        assertThat(applicationSettings.toString(), is(applicationSettings.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
        applicationSettings.load();
        applicationSettings.set(key, null);
        assertThat(applicationSettings.get(key), is(nullValue()));
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

    private void prepareExceptionTest() throws NoSuchFieldException {
        setFieldValue(applicationSettings, "properties", null);
        expectedException.expect(ApplicationSettingsException.class);
        expectedException.expectMessage("Application settings not loaded, first invokes load()");
    }

}