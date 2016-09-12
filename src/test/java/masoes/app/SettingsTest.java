/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SettingsTest {

    @Test
    public void shouldReturnCorrectValue() {
        SettingsLoader expectedSettingsLoader = SettingsLoader.getInstance();
        assertThat(expectedSettingsLoader.get("app.name"), is(Settings.APP_NAME.getValue()));
    }

}