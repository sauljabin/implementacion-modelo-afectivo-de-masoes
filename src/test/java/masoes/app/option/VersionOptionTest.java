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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class VersionOptionTest {

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private VersionOption versionOption;
    private ApplicationSettings mockApplicationSettings;

    @Before
    public void setUp() throws Exception {
        mockApplicationSettings = mock(ApplicationSettings.class);
        versionOption = new VersionOption();
        setFieldValue(versionOption, "applicationSettings", mockApplicationSettings);
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
        String appName = "APPNAME";
        String appVersion = "appVersion";
        String appRevision = "appRevision";
        String jadeVersion = "jadeVersion";
        String jadeRevision = "jadeRevision";

        when(mockApplicationSettings.get(ApplicationSettings.APP_NAME)).thenReturn(appName);
        when(mockApplicationSettings.get(ApplicationSettings.APP_VERSION)).thenReturn(appVersion);
        when(mockApplicationSettings.get(ApplicationSettings.APP_REVISION)).thenReturn(appRevision);
        when(mockApplicationSettings.get(ApplicationSettings.JADE_VERSION)).thenReturn(jadeVersion);
        when(mockApplicationSettings.get(ApplicationSettings.JADE_REVISION)).thenReturn(jadeRevision);

        String expectedStringMasoes = String.format("%s\nVersion: %s\nRevision: %s", appName, appVersion, appRevision);
        String expectedStringJade = String.format("JADE\nVersion: %s\nRevision: %s\n", jadeVersion, jadeRevision);
        versionOption.exec();
        assertThat(systemOutRule.getLog(), containsString(expectedStringMasoes));
        assertThat(systemOutRule.getLog(), containsString(expectedStringJade));
    }

}