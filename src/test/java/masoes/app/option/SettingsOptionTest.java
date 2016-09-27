/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import masoes.app.setting.Setting;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SettingsOptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SettingsOption settingsOption;
    private ApplicationLogger mockLogger;

    @Before
    public void setUp() throws Exception {
        mockLogger = mock(ApplicationLogger.class);
        settingsOption = new SettingsOption(mockLogger);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(settingsOption.getOpt(), is("s"));
        assertThat(settingsOption.getLongOpt(), is("settings"));
        assertThat(settingsOption.getDescription(), containsString("Sets application settings"));
        assertTrue(settingsOption.hasArg());
        assertThat(settingsOption.getOrder(), is(30));
    }

    @Test
    public void shouldSetSettings() {
        String expectedKey1 = "expectedKey1";
        String expectedValue1 = "value1";
        String expectedKey2 = "expectedKey2";
        String expectedValue2 = "value2";

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(expectedKey1, expectedValue1);
        expectedMap.put(expectedKey2, expectedValue2);

        settingsOption.exec(expectedMap.toString());

        assertThat(Setting.get(expectedKey1), is(expectedValue1));
        assertThat(Setting.get(expectedKey2), is(expectedValue2));
    }

    @Test
    public void shouldThrowInvalidParameterWhenNoExistValueException() {
        String expectedArgs = "{setting1=,setting2=value2}";
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Incorrect settings format: " + expectedArgs);
        settingsOption.exec(expectedArgs);
    }

    @Test
    public void shouldInvokeLoggerErrorWhenException() {
        String expectedArgs = "{setting1=value1,setting2=value2}";
        settingsOption.exec(expectedArgs);
        verify(mockLogger).updatedSettings(eq(Setting.toMap()));
    }

}