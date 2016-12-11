/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.settings.ApplicationSettings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VersionOptionTest {

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private VersionOption versionOption;
    private ApplicationSettings applicationSettings;

    @Before
    public void setUp() {
        applicationSettings = ApplicationSettings.getInstance();
        applicationSettings.load();
        versionOption = new VersionOption();
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(versionOption.getOpt(), is("v"));
        assertThat(versionOption.getLongOpt(), is("version"));
        assertThat(versionOption.getDescription(), is("Shows the application version"));
        assertThat(versionOption.getArgType(), is(ArgumentType.NO_ARGS));
        assertThat(versionOption.getOrder(), is(10));
    }

    @Test
    public void shouldPrintVersion() {
        String expectedStringMasoes = String.format("%s\nVersion: %s\nRevision: %s", applicationSettings.get(ApplicationSettings.APP_NAME).toUpperCase(), applicationSettings.get(ApplicationSettings.APP_VERSION), applicationSettings.get(ApplicationSettings.APP_REVISION));
        String expectedStringJade = String.format("JADE\nVersion: %s\nRevision: %s\n", applicationSettings.get(ApplicationSettings.JADE_VERSION), applicationSettings.get(ApplicationSettings.JADE_REVISION));
        versionOption.exec();
        assertThat(systemOutRule.getLog(), containsString(expectedStringMasoes));
        assertThat(systemOutRule.getLog(), containsString(expectedStringJade));
    }

}