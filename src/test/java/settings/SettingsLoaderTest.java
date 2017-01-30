/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class SettingsLoaderTest {

    private static final String PATH = "application.properties";
    private static final String KEY = "KEY";
    private static final String VALUE = "VALUE";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private SettingsLoader jadeSettingsLoader;

    @Before
    public void setUp() {
        jadeSettingsLoader = new SettingsLoader();
        jadeSettingsLoader.load(PATH);
    }

    @Test
    public void shouldGetCorrectSetting() {
        jadeSettingsLoader.set(KEY, VALUE);
        assertThat(jadeSettingsLoader.get(KEY), is(VALUE));
    }

    @Test
    public void shouldClearSettingsWhenLoad() {
        Map<String, String> expectedToMap = jadeSettingsLoader.toMap();
        jadeSettingsLoader.set(KEY, VALUE);
        jadeSettingsLoader.load(PATH);
        Map<String, String> actualToMap = jadeSettingsLoader.toMap();
        assertReflectionEquals(expectedToMap, actualToMap);
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        String expectedDefaultValue = "defaultValue";
        assertThat(jadeSettingsLoader.get("", expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatKeyIsNull() {
        String expectedDefaultValue = "defaultValue";
        assertThat(jadeSettingsLoader.get(null, expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldReturnNullWhenKeyIsNull() {
        assertThat(jadeSettingsLoader.get(null), is(nullValue()));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        jadeSettingsLoader.set(KEY, VALUE);
        assertThat(jadeSettingsLoader.get(KEY, "anything"), is(VALUE));
    }

    @Test
    public void shouldRemoveProperty() {
        jadeSettingsLoader.set(KEY, VALUE);
        assertThat(jadeSettingsLoader.get(KEY), is(VALUE));
        jadeSettingsLoader.set(KEY, null);
        assertThat(jadeSettingsLoader.get(KEY), is(nullValue()));
    }

    @Test
    public void shouldThrowsExceptionWhenErrorInLoad() throws Exception {
        String expectedMessage = "Message";
        Properties propertiesMock = mock(Properties.class);
        doThrow(new IOException(expectedMessage)).when(propertiesMock).load(any(InputStream.class));
        setFieldValue(jadeSettingsLoader, "properties", propertiesMock);
        expectedException.expect(SettingsException.class);
        expectedException.expectMessage(expectedMessage);
        jadeSettingsLoader.load(PATH);
    }

}