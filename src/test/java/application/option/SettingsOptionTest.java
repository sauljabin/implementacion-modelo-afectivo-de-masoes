/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import logger.application.ApplicationLogger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import settings.application.ApplicationSettings;

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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SettingsOption settingsOption;
    private ApplicationLogger loggerMock;
    private ApplicationSettings applicationSettingsMock;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        loggerMock = mock(ApplicationLogger.class);
        settingsOption = new SettingsOption();
        setFieldValue(settingsOption, "logger", loggerMock);
        setFieldValue(settingsOption, "applicationSettings", applicationSettingsMock);
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
    public void shouldSetSettings() {
        String expectedKey1 = "expectedKey1";
        String expectedValue1 = "value1";
        String expectedKey2 = "expectedKey2";
        String expectedValue2 = "value2";

        Properties expectedProperties = new Properties();
        expectedProperties.put(expectedKey1, expectedValue1);
        expectedProperties.put(expectedKey2, expectedValue2);

        settingsOption.setProperties(expectedProperties);
        settingsOption.exec();

        verify(applicationSettingsMock).set(expectedKey1, expectedValue1);
        verify(applicationSettingsMock).set(expectedKey2, expectedValue2);
        verify(loggerMock).updatedSettings();
    }

}