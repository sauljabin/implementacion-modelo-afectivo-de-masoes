/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import logger.writer.ApplicationLogger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import settings.loader.ApplicationSettings;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class SettingsOptionTest {

    private static final String ENV = "masoes.env";
    private static final String ENV_VALUE = "dummy";
    private static final String APP_NAME = "app.name";
    private static final String APP_NAME_VALUE = "masoes";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private SettingsOption settingsOption;
    private ApplicationLogger loggerMock;
    private ApplicationSettings applicationSettingsMock;
    private Properties properties;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        loggerMock = mock(ApplicationLogger.class);

        settingsOption = new SettingsOption();
        setFieldValue(settingsOption, "logger", loggerMock);
        setFieldValue(settingsOption, "applicationSettings", applicationSettingsMock);

        properties = new Properties();
        properties.put(ENV, ENV_VALUE);
        properties.put(APP_NAME, APP_NAME_VALUE);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(settingsOption.getOpt(), is("S"));
        assertThat(settingsOption.getLongOpt(), is(nullValue()));
        assertThat(settingsOption.getDescription(), containsString("Sets application settings"));
        assertThat(settingsOption.getArgType(), is(ArgumentType.UNLIMITED_ARGS));
        assertThat(settingsOption.getOrder(), is(30));
        assertFalse(settingsOption.isFinalOption());
    }

    @Test
    public void shouldSetApplicationSettings() {
        settingsOption.setProperties(properties);
        settingsOption.exec();
        verify(applicationSettingsMock).set(ENV, ENV_VALUE);
        verify(applicationSettingsMock).set(APP_NAME, APP_NAME_VALUE);
        verify(loggerMock).updatedSettings();
    }

}