/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class SettingTest {

    private SettingsLoader settingsLoader = SettingsLoader.getInstance();

    @Before
    public void setUp() {
        settingsLoader.load();
    }

    @Test
    public void shouldReturnCorrectValue() {
        assertThat(Setting.APP_NAME.getValue(), is(settingsLoader.getSetting("app.name")));
    }

    @Test
    public void shouldSetValue() {
        String expectedValue = "testValue";
        Setting.APP_NAME.setValue(expectedValue);
        assertThat(Setting.APP_NAME.getValue(), is(expectedValue));
    }

    @Test
    public void shouldReturnDefaultValue() {
        Setting.APP_NAME.setValue(null);
        assertThat(Setting.APP_NAME.getValue("default"), is("default"));
        assertThat(Setting.get("key", "default"), is("default"));
    }

    @Test
    public void shouldSetAndGetNewSetting() {
        String keySetting = "newSetting";
        String valueSetting = "valueSetting";
        Setting.set(keySetting, valueSetting);
        assertThat(Setting.get(keySetting), is(settingsLoader.getSetting(keySetting)));
    }

    @Test
    public void shouldGetMap() {
        assertReflectionEquals(SettingsLoader.getInstance().toMap(), Setting.toMap());
    }

    @Test
    public void shouldBeEqualsKeyAndToString() {
        assertThat(Setting.APP_NAME.getKey(), is(Setting.APP_NAME.toString()));
    }

    @Test
    public void shouldHasAllEnumsValues() {
        List<String> actualEnumKeys = Arrays
                .stream(Setting.values())
                .map(setting -> setting.getKey())
                .sorted()
                .collect(Collectors.toList());
        List<String> expectedEnumKeys = settingsLoader.getKeys();
        assertReflectionEquals(actualEnumKeys, expectedEnumKeys);
    }

}