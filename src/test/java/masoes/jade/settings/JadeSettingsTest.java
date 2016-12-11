/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.settings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
        key = "key";
        expectedValue = "expectedValue";
    }

    @Test
    public void shouldGetSameInstance() {
        JadeSettings actualJadeSettings = JadeSettings.getInstance();
        assertThat(actualJadeSettings, is(jadeSettings));
    }

    @Test
    public void shouldThrowsExceptionInSetWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        jadeSettings.set(key, expectedValue);
    }

    @Test
    public void shouldThrowsExceptionInGetWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        jadeSettings.get(key);
    }

    @Test
    public void shouldThrowsExceptionInGetDefaultWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        jadeSettings.get(key, "default");
    }

    @Test
    public void shouldThrowsExceptionInToMapWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        jadeSettings.toMap();
    }

    @Test
    public void shouldThrowsExceptionInToStringWhenSettingsIsNotLoaded() throws NoSuchFieldException {
        prepareExceptionTest();
        jadeSettings.toString();
    }

    @Test
    public void shouldGetCorrectSetting() {
        jadeSettings.load();
        jadeSettings.set(key, expectedValue);
        assertThat(jadeSettings.get(key), is(expectedValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatNotExistKey() {
        jadeSettings.load();
        String expectedDefaultValue = "defaultValue";
        assertThat(jadeSettings.get("", expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldGetDefaultSettingInCaseThatKeyIsNull() {
        jadeSettings.load();
        String expectedDefaultValue = "defaultValue";
        assertThat(jadeSettings.get(null, expectedDefaultValue), is(expectedDefaultValue));
    }

    @Test
    public void shouldReturnNullThatKeyIsNull() {
        jadeSettings.load();
        assertThat(jadeSettings.get(null), is(nullValue()));
    }

    @Test
    public void shouldNotGetDefaultSettingInCaseThatExistKey() {
        jadeSettings.load();
        jadeSettings.set(key, expectedValue);
        assertThat(jadeSettings.get(key, "anything"), is(expectedValue));
    }

    @Test
    public void shouldGetTheSameStringThatAMap() {
        jadeSettings.load();
        assertThat(jadeSettings.toString(), is(jadeSettings.toMap().toString()));
    }

    @Test
    public void shouldRemoveProperty() {
        jadeSettings.load();
        jadeSettings.set(key, expectedValue);
        assertThat(jadeSettings.get(key), is(expectedValue));
        jadeSettings.set(key, null);
        assertThat(jadeSettings.get(key), is(nullValue()));
    }

    private void prepareExceptionTest() throws NoSuchFieldException {
        setFieldValue(jadeSettings, "properties", null);
        expectedException.expect(JadeSettingsException.class);
        expectedException.expectMessage("Jade settings not loaded, first invokes load()");
    }

}