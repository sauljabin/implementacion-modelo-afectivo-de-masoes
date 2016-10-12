/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class VersionOptionTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    private VersionOption versionOption;
    private SettingsLoader settingsLoader = SettingsLoader.getInstance();

    @Before
    public void setUp() {
        settingsLoader.load();
        versionOption = new VersionOption();
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(versionOption.getOpt(), is("v"));
        assertThat(versionOption.getLongOpt(), is("version"));
        assertThat(versionOption.getDescription(), is("Shows the application version"));
        assertFalse(versionOption.hasArg());
        assertThat(versionOption.getOrder(), is(10));
    }

    @Test
    public void shouldPrintVersion() {
        String expectedStringMasoes = String.format("%s\nVersion: %s\nRevision: %s", Setting.APP_NAME.getValue().toUpperCase(), Setting.APP_VERSION.getValue(), Setting.APP_REVISION.getValue());
        String expectedStringJade = String.format("JADE\nVersion: %s\nRevision: %s\n", Setting.JADE_VERSION.getValue(), Setting.JADE_REVISION.getValue());
        versionOption.exec("");
        assertThat(systemOutRule.getLog(), containsString(expectedStringMasoes));
        assertThat(systemOutRule.getLog(), containsString(expectedStringJade));
    }

}