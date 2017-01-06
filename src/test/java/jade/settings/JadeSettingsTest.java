/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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

public class JadeSettingsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JadeSettings jadeSettings;
    private String key;
    private String expectedValue;

    @Before
    public void setUp() {
        jadeSettings = JadeSettings.getInstance();
        jadeSettings.load();
        key = "key";
        expectedValue = "expectedValue";
    }

    @Test
    public void shouldGetSameInstance() {
        JadeSettings actualJadeSettings = JadeSettings.getInstance();
        assertThat(actualJadeSettings, is(jadeSettings));
    }

    @Test
    public void shouldThrowsExceptionWhenErrorInLoad() throws Exception {
        String expectedMessage = "Message";
        Properties mockProperties = mock(Properties.class);
        doThrow(new IOException(expectedMessage)).when(mockProperties).load(any(InputStream.class));
        setFieldValue(jadeSettings, "properties", mockProperties);
        expectedException.expect(JadeSettingsException.class);
        expectedException.expectMessage(expectedMessage);
        jadeSettings.load();
    }

    @Test
    public void shouldGetCorrectSetting() {
        jadeSettings.set(key, expectedValue);
        assertThat(jadeSettings.get(key), is(expectedValue));
    }

    @Test
    public void shouldClearSettingsWhenLoad() {
        Map<String, String> expectedToMap = jadeSettings.toMap();
        jadeSettings.set(key, expectedValue);
        jadeSettings.load();
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
        jadeSettings.set(key, expectedValue);
        assertThat(jadeSettings.get(key, "anything"), is(expectedValue));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        assertThat(jadeSettings.toString(), is(jadeSettings.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
        jadeSettings.set(key, expectedValue);
        assertThat(jadeSettings.get(key), is(expectedValue));
        jadeSettings.set(key, null);
        assertThat(jadeSettings.get(key), is(nullValue()));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = getInitValues();
        expectedValues.keySet().forEach(key -> assertThat(jadeSettings.get(key), is(expectedValues.get(key))));
    }

    private Map<String, String> getInitValues() {
        Map<String, String> initValues = new HashMap<>();
        initValues.put("gui", "true");
        initValues.put("port", "1099");
        initValues.put("jade_mtp_http_port", "7778");
        initValues.put("jade_domain_df_autocleanup", "true");
        initValues.put("platform-id", "masoes");
        return initValues;
    }

}