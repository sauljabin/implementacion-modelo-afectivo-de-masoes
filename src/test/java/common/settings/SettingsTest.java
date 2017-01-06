/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package common.settings;

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

public class SettingsTest {

    private static final String PATH = "application.properties";
    private static final String KEY = "KEY";
    private static final String VALUE = "VALUE";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Settings jadeSettings;

    @Before
    public void setUp() throws Exception {
        jadeSettings = new Settings();
        jadeSettings.load(PATH);
    }

    @Test
    public void shouldGetCorrectSetting() {
        jadeSettings.set(KEY, VALUE);
        assertThat(jadeSettings.get(KEY), is(VALUE));
    }

    @Test
    public void shouldClearSettingsWhenLoad() throws Exception {
        Map<String, String> expectedToMap = jadeSettings.toMap();
        jadeSettings.set(KEY, VALUE);
        jadeSettings.load(PATH);
        Map<String, String> actualToMap = jadeSettings.toMap();
        assertReflectionEquals(expectedToMap, actualToMap);
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        String expectedDefaultValue = "defaultValue";
        assertThat(jadeSettings.get("", expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatKeyIsNull() {
        String expectedDefaultValue = "defaultValue";
        assertThat(jadeSettings.get(null, expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldReturnNullThatKeyIsNull() {
        assertThat(jadeSettings.get(null), is(nullValue()));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        jadeSettings.set(KEY, VALUE);
        assertThat(jadeSettings.get(KEY, "anything"), is(VALUE));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        assertThat(jadeSettings.toString(), is(jadeSettings.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
        jadeSettings.set(KEY, VALUE);
        assertThat(jadeSettings.get(KEY), is(VALUE));
        jadeSettings.set(KEY, null);
        assertThat(jadeSettings.get(KEY), is(nullValue()));
    }

    @Test
    public void shouldThrowsExceptionWhenErrorInLoad() throws Exception {
        String expectedMessage = "Message";
        Properties mockProperties = mock(Properties.class);
        doThrow(new IOException(expectedMessage)).when(mockProperties).load(any(InputStream.class));
        setFieldValue(jadeSettings, "properties", mockProperties);
        expectedException.expect(SettingsException.class);
        expectedException.expectMessage(expectedMessage);
        jadeSettings.load(PATH);
    }

}