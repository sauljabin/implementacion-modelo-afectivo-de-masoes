/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import masoes.app.Main;
import masoes.app.option.VersionOption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class ApplicationLoggerTest {

    private Logger mockLogger;
    private Class<Main> expectedClass;
    private ApplicationLogger applicationLogger;

    @Before
    public void setUp() throws Exception {
        mockLogger = mock(Logger.class);
        expectedClass = Main.class;
        PowerMockito.mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(expectedClass)).thenReturn(mockLogger);
        applicationLogger = ApplicationLogger.newInstance(expectedClass);
    }

    @Test
    public void shouldLogStartingApp() {
        String[] expectedArgs = {"-h"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("app", "masoes");
        applicationLogger.startingApplication(expectedArgs, expectedMap);
        verify(mockLogger).info(eq("Starting application with arguments: [-h], and settings {app=masoes}"));
    }

    @Test
    public void shouldLogCantNotStartApp() {
        Exception expectedException = new Exception();
        applicationLogger.cantNotStartApplication(expectedException);
        verify(mockLogger).error(eq("Could not start the application"), eq(expectedException));
    }

    @Test
    public void shouldLogStartingOption() {
        VersionOption applicationOption = new VersionOption();
        applicationLogger.startingOption(applicationOption);
        verify(mockLogger).info(eq("Starting option with arguments: " + applicationOption.toString()));
    }

}