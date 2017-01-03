/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.logger.ApplicationLogger;
import application.settings.ApplicationSettings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    private ApplicationLogger mockLogger;
    private ApplicationSettings mockApplicationSettings;

    @Before
    public void setUp() throws Exception {
        mockApplicationSettings = mock(ApplicationSettings.class);
        mockLogger = mock(ApplicationLogger.class);
        settingsOption = new SettingsOption();
        setFieldValue(settingsOption, "logger", mockLogger);
        setFieldValue(settingsOption, "applicationSettings", mockApplicationSettings);
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

        verify(mockApplicationSettings).set(expectedKey1, expectedValue1);
        verify(mockApplicationSettings).set(expectedKey2, expectedValue2);
        verify(mockLogger).updatedSettings();
    }

}