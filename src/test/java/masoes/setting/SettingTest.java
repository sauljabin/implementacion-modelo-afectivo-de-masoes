/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.setting;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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

}