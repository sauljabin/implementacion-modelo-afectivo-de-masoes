/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ArgumentType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import settings.loader.ApplicationSettings;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class VersionOptionTest {

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private VersionOption versionOption;
    private ApplicationSettings applicationSettingsMock;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        versionOption = new VersionOption();
        setFieldValue(versionOption, "applicationSettings", applicationSettingsMock);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(versionOption.getOpt(), is("v"));
        assertThat(versionOption.getLongOpt(), is("version"));
        assertThat(versionOption.getDescription(), is("Shows the application version"));
        assertThat(versionOption.getArgType(), is(ArgumentType.NO_ARGS));
        assertThat(versionOption.getOrder(), is(10));
        assertTrue(versionOption.isFinalOption());
    }

    @Test
    public void shouldPrintVersion() {
        String appName = "APPNAME";
        String appVersion = "appVersion";
        String appRevision = "appRevision";
        String jadeVersion = "jadeVersion";
        String jadeRevision = "jadeRevision";

        doReturn(appName).when(applicationSettingsMock).get(ApplicationSettings.APP_NAME);
        doReturn(appVersion).when(applicationSettingsMock).get(ApplicationSettings.APP_VERSION);
        doReturn(appRevision).when(applicationSettingsMock).get(ApplicationSettings.APP_REVISION);
        doReturn(jadeVersion).when(applicationSettingsMock).get(ApplicationSettings.JADE_VERSION);
        doReturn(jadeRevision).when(applicationSettingsMock).get(ApplicationSettings.JADE_REVISION);

        String expectedStringMasoes = String.format("%s\nVersion: %s\nRevision: %s", appName, appVersion, appRevision);
        String expectedStringJade = String.format("JADE\nVersion: %s\nRevision: %s\n", jadeVersion, jadeRevision);
        versionOption.exec();
        assertThat(systemOutRule.getLog(), containsString(expectedStringMasoes));
        assertThat(systemOutRule.getLog(), containsString(expectedStringJade));
    }

}