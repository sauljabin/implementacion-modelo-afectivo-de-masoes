/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.settings;

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

    private static final String KEY = "KEY";
    private static final String EXPECTED_VALUE = "VALUE";
    private static final String APP_NAME = "app.name";
    private static final String APP_REVISION = "app.revision";
    private static final String APP_VERSION = "app.version";
    private static final String OS_NAME = "os.name";
    private static final String OS_ARCH = "os.arch";
    private static final String OS_VERSION = "os.version";
    private static final String JAVA_VERSION = "java.version";
    private static final String JAVA_VENDOR = "java.vendor";
    private static final String JADE_VERSION = "jade.version";
    private static final String JADE_REVISION = "jade.revision";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ApplicationSettings applicationSettings;

    @Before
    public void setUp() {
        applicationSettings = ApplicationSettings.getInstance();
        applicationSettings.load();
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
        applicationSettings.set(KEY, EXPECTED_VALUE);
        assertThat(applicationSettings.get(KEY), is(EXPECTED_VALUE));
    }

    @Test
    public void shouldClearSettingsWhenLoad() {
        Map<String, String> expectedToMap = applicationSettings.toMap();
        applicationSettings.set(KEY, EXPECTED_VALUE);
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
        applicationSettings.set(KEY, EXPECTED_VALUE);
        assertThat(applicationSettings.get(KEY, "anything"), is(EXPECTED_VALUE));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = getInitValues();
        expectedValues.keySet().forEach(
                key -> assertThat(applicationSettings.get(key), is(expectedValues.get(key)))
        );
        assertThat(applicationSettings.get(APP_NAME), is(notNullValue()));
        assertThat(applicationSettings.get(APP_REVISION), is(notNullValue()));
        assertThat(applicationSettings.get(APP_VERSION), is(notNullValue()));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        assertThat(applicationSettings.toString(), is(applicationSettings.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
        applicationSettings.set(KEY, null);
        assertThat(applicationSettings.get(KEY), is(nullValue()));
    }

    private Map<String, String> getInitValues() {
        Map<String, String> initValues = new HashMap<>();
        initValues.put(OS_NAME, System.getProperty(OS_NAME));
        initValues.put(OS_ARCH, System.getProperty(OS_ARCH));
        initValues.put(OS_VERSION, System.getProperty(OS_VERSION));
        initValues.put(JAVA_VERSION, System.getProperty(JAVA_VERSION));
        initValues.put(JAVA_VENDOR, System.getProperty(JAVA_VENDOR));
        initValues.put(JADE_VERSION, jade.core.Runtime.getVersion());
        initValues.put(JADE_REVISION, jade.core.Runtime.getRevision());
        return initValues;
    }

}
