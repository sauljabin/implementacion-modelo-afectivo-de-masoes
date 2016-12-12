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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
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
        applicationSettings.load();
        key = "key";
        expectedValue = "expectedValue";
    }

    @Test
    public void shouldGetSameInstance() {
        ApplicationSettings expectedApplicationSettings = ApplicationSettings.getInstance();
        assertThat(applicationSettings, is(expectedApplicationSettings));
    }

    @Test
    public void shouldThrowsExceptionWhenErrorInLoad() throws Exception {
        String expectedMessage = "Message";
        Properties mockProperties = mock(Properties.class);
        doThrow(new IOException(expectedMessage)).when(mockProperties).load(any(InputStream.class));
        setFieldValue(applicationSettings, "properties", mockProperties);
        expectedException.expect(ApplicationSettingsException.class);
        expectedException.expectMessage(expectedMessage);
        applicationSettings.load();
    }

    @Test
    public void shouldGetCorrectSetting() {
        applicationSettings.set(key, expectedValue);
        assertThat(applicationSettings.get(key), is(expectedValue));
    }

    @Test
    public void shouldClearSettingsWhenLoad() {
        Map<String, String> expectedToMap = applicationSettings.toMap();
        applicationSettings.set(key, expectedValue);
        applicationSettings.load();
        Map<String, String> actualToMap = applicationSettings.toMap();
        assertReflectionEquals(expectedToMap, actualToMap);
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        String expectedDefaultValue = "defaultValue";
        assertThat(applicationSettings.get("", expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatKeyIsNull() {
        String expectedDefaultValue = "defaultValue";
        assertThat(applicationSettings.get(null, expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldReturnNullThatKeyIsNull() {
        assertThat(applicationSettings.get(null), is(nullValue()));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        applicationSettings.set(key, expectedValue);
        assertThat(applicationSettings.get(key, "anything"), is(expectedValue));
    }

    @Test
    public void shouldLoadInitValues() {
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
        assertThat(applicationSettings.toString(), is(applicationSettings.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
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
