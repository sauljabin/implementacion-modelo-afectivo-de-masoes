/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class SettingTest {

    private final SettingsLoader expectedSettingsLoader = SettingsLoader.getInstance();

    @Test
    public void shouldReturnCorrectValue() {
        expectedSettingsLoader.load();
        assertThat(Setting.APP_NAME.getValue(), is(expectedSettingsLoader.getSetting("app.name")));
    }

    @Test
    public void shouldSetValue() {
        expectedSettingsLoader.load();
        String expectedValue = "testValue";
        Setting.APP_NAME.setValue(expectedValue);
        assertThat(Setting.APP_NAME.getValue(), is(expectedValue));
    }

    @Test
    public void shouldReturnDefaultValue() {
        expectedSettingsLoader.clear();
        assertThat(Setting.APP_NAME.getValue("default"), is("default"));
        assertThat(Setting.get("key", "default"), is("default"));
    }

    @Test
    public void shouldSetAndGetNewSetting() {
        expectedSettingsLoader.load();
        String keySetting = "newSetting";
        String valueSetting = "valueSetting";
        Setting.set(keySetting, valueSetting);
        assertThat(Setting.get(keySetting), is(expectedSettingsLoader.getSetting(keySetting)));
    }

    @Test
    public void shouldGetMap() {
        expectedSettingsLoader.load();
        assertReflectionEquals(SettingsLoader.getInstance().toMap(), Setting.toMap());
    }

    @Test
    public void shouldBeEqualsKeyAndToString() {
        assertThat(Setting.APP_NAME.getKey(), is(Setting.APP_NAME.toString()));
    }

}