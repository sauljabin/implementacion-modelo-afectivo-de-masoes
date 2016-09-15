/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.setting;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SettingsTest {

    private final SettingsLoader expectedSettingsLoader = SettingsLoader.getInstance();

    @Test
    public void shouldReturnCorrectValue() {
        expectedSettingsLoader.load();
        assertThat(Settings.APP_NAME.getValue(), is(expectedSettingsLoader.get("app.name")));
    }

    @Test
    public void shouldReturnDefaultValue() {
        expectedSettingsLoader.clear();
        assertThat(Settings.APP_NAME.getValue("default"), is("default"));
    }

}