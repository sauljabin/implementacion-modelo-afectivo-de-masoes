/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class EnvironmentOptionTest {

    private EnvironmentOption environmentOption;
    private SettingsLoader settingsLoader;

    @Before
    public void setUp() {
        settingsLoader = SettingsLoader.getInstance();
        settingsLoader.load();
        environmentOption = new EnvironmentOption();
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(environmentOption.getOpt(), is("e"));
        assertThat(environmentOption.getLongOpt(), is("env"));
        assertThat(environmentOption.getDescription(), is("Sets the environment for case study"));
        assertTrue(environmentOption.hasArg());
        assertThat(environmentOption.getOrder(), is(50));
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String expectedCaseStudy = "default";
        environmentOption.exec(expectedCaseStudy);
        assertThat(Setting.MASOES_ENV.getValue(), is(expectedCaseStudy));
        assertThat(Setting.get("masoes.env"), is(expectedCaseStudy));
    }

}