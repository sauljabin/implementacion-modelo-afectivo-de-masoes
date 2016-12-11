/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class SettingsOptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SettingsOption settingsOption;
    private ApplicationLogger mockLogger;
    private SettingsLoader settingsLoader;

    @Before
    public void setUp() throws Exception {
        settingsLoader = SettingsLoader.getInstance();
        settingsLoader.load();
        mockLogger = mock(ApplicationLogger.class);
        settingsOption = new SettingsOption();
        setFieldValue(settingsOption, "logger", mockLogger);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(settingsOption.getOpt(), is("S"));
        assertThat(settingsOption.getLongOpt(), is(nullValue()));
        assertThat(settingsOption.getDescription(), containsString("Sets application settings"));
        assertThat(settingsOption.getArgType(), is(ArgumentType.UNLIMITED_ARGS));
        assertThat(settingsOption.getOrder(), is(30));
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

        assertThat(Setting.get(expectedKey1), is(expectedValue1));
        assertThat(Setting.get(expectedKey2), is(expectedValue2));
        verify(mockLogger).updatedSettings();
    }

}