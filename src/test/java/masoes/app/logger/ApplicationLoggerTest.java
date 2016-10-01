/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import masoes.app.option.ApplicationOption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class ApplicationLoggerTest {

    private Logger mockLogger;
    private Class<ApplicationLoggerTest> expectedClass;
    private ApplicationLogger applicationLogger;
    private Map<String, String> expectedMap;

    @Before
    public void setUp() throws Exception {
        mockLogger = mock(Logger.class);
        mockStatic(LoggerFactory.class);

        expectedClass = ApplicationLoggerTest.class;
        when(LoggerFactory.getLogger(expectedClass)).thenReturn(mockLogger);

        applicationLogger = ApplicationLogger.newInstance(expectedClass);
        expectedMap = new HashMap<>();
        expectedMap.put("app", "masoes");

    }

    @Test
    public void shouldLogStartingApp() {
        String[] args = {"-h"};
        applicationLogger.startingApplication(args, expectedMap);
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
        ApplicationOption applicationOption = mock(ApplicationOption.class);
        String expectedToString = "expectedToString";
        when(applicationOption.toString()).thenReturn(expectedToString);

        applicationLogger.startingOption(applicationOption);
        verify(mockLogger).info(eq("Starting option: " + expectedToString));
    }

    @Test
    public void shouldLogUpdatedSettings() {
        applicationLogger.updatedSettings(expectedMap);
        verify(mockLogger).info(eq("Updated settings: {app=masoes}"));
    }

}