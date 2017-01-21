/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger.application;

import application.option.ApplicationOption;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import settings.application.ApplicationSettings;
import settings.jade.JadeSettings;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ApplicationLoggerTest {

    private Logger mockLogger;
    private ApplicationLogger applicationLogger;
    private ApplicationSettings mockApplicationSettings;
    private JadeSettings mockJadeSettings;
    private Map<String, String> expectedApplicationSettingsMap;
    private Map<String, String> expectedJadeSettingsMap;

    @Before
    public void setUp() throws Exception {
        mockApplicationSettings = mock(ApplicationSettings.class);
        mockJadeSettings = mock(JadeSettings.class);
        mockLogger = mock(Logger.class);
        applicationLogger = new ApplicationLogger(mockLogger);

        expectedApplicationSettingsMap = new HashMap<>();
        expectedApplicationSettingsMap.put("key", "value");
        doReturn(expectedApplicationSettingsMap).when(mockApplicationSettings).toMap();
        doCallRealMethod().when(mockApplicationSettings).toString();

        expectedJadeSettingsMap = new HashMap<>();
        expectedJadeSettingsMap.put("key", "value");
        doReturn(expectedJadeSettingsMap).when(mockJadeSettings).toMap();
        doCallRealMethod().when(mockJadeSettings).toString();

        setFieldValue(applicationLogger, "jadeSettings", mockJadeSettings);
        setFieldValue(applicationLogger, "applicationSettings", mockApplicationSettings);
    }

    @Test
    public void shouldLogStartingApp() {
        String[] args = {"-h"};
        applicationLogger.startingApplication(args);
        verify(mockLogger).info(eq("Starting application with arguments: [-h], settings: " + expectedApplicationSettingsMap.toString() + ", jade settings: " + expectedJadeSettingsMap.toString()));
    }

    @Test
    public void shouldLogClosingApp() {
        applicationLogger.closingApplication();
        verify(mockLogger).info(eq("Closing application"));
    }

    @Test
    public void shouldLogCantNotStartApp() {
        Exception expectedException = new Exception();
        applicationLogger.cantNotStartApplication(expectedException);
        verify(mockLogger).error(contains("Could not start the application"), eq(expectedException));
    }

    @Test
    public void shouldLogStartingOption() {
        ApplicationOption applicationOption = mock(ApplicationOption.class);
        String expectedToString = "expectedToString";
        doReturn(expectedToString).when(applicationOption).toString();

        applicationLogger.startingOption(applicationOption);
        verify(mockLogger).info(eq("Starting option: " + expectedToString));
    }

    @Test
    public void shouldLogUpdatedSettings() {
        applicationLogger.updatedSettings();
        verify(mockLogger).info(eq("Updated settings: " + expectedApplicationSettingsMap.toString() + ", jade settings: " + expectedJadeSettingsMap.toString()));
    }

    @Test
    public void shouldLogException() {
        Exception expectedException = new Exception("error");
        applicationLogger.exception(expectedException);
        verify(mockLogger).error(eq("Exception: " + expectedException.getMessage()), eq(expectedException));
    }

}