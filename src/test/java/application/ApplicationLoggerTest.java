/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

import jade.JadeSettings;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

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

    private Logger loggerMock;
    private ApplicationLogger applicationLogger;
    private ApplicationSettings applicationSettingsMock;
    private JadeSettings jadeSettingsMock;
    private Map<String, String> expectedApplicationSettingsMap;
    private Map<String, String> expectedJadeSettingsMap;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        jadeSettingsMock = mock(JadeSettings.class);
        loggerMock = mock(Logger.class);
        applicationLogger = new ApplicationLogger(new Object());

        expectedApplicationSettingsMap = new HashMap<>();
        expectedApplicationSettingsMap.put("application", "value");
        doReturn(expectedApplicationSettingsMap).when(applicationSettingsMock).toMap();
        doCallRealMethod().when(applicationSettingsMock).toString();

        expectedJadeSettingsMap = new HashMap<>();
        expectedJadeSettingsMap.put("jade", "value");
        doReturn(expectedJadeSettingsMap).when(jadeSettingsMock).toMap();
        doCallRealMethod().when(jadeSettingsMock).toString();

        setFieldValue(applicationLogger, "jadeSettings", jadeSettingsMock);
        setFieldValue(applicationLogger, "applicationSettings", applicationSettingsMock);
        setFieldValue(applicationLogger, "logger", loggerMock);
    }

    @Test
    public void shouldLogStartingAppWithArgs() {
        String[] args = {"-h"};
        applicationLogger.startingApplication(args);
        verify(loggerMock).info(eq("Starting application with arguments: [-h], settings: " + applicationSettingsMock.toString() + ", jade settings: " + jadeSettingsMock.toString()));
    }

    @Test
    public void shouldLogClosingApp() {
        applicationLogger.closingApplication();
        verify(loggerMock).info(eq("Closing application"));
    }

    @Test
    public void shouldLogCantNotStartApp() {
        Exception expectedException = new Exception();
        applicationLogger.cantNotStartApplication(expectedException);
        verify(loggerMock).error(contains("Could not start the application"), eq(expectedException));
    }

    @Test
    public void shouldLogStartingOption() {
        ApplicationOption applicationOption = mock(ApplicationOption.class);
        String expectedToString = "expectedToString";
        doReturn(expectedToString).when(applicationOption).toString();

        applicationLogger.startingOption(applicationOption);
        verify(loggerMock).info(eq("Starting option: " + expectedToString));
    }

    @Test
    public void shouldLogUpdatedSettings() {
        applicationLogger.updatedSettings();
        verify(loggerMock).info(eq("Updated settings: " + applicationSettingsMock.toString() + ", jade settings: " + jadeSettingsMock.toString()));
    }

    @Test
    public void shouldLogException() {
        Exception expectedException = new Exception("error");
        applicationLogger.exception(expectedException);
        verify(loggerMock).error(eq("Exception: " + expectedException.getMessage()), eq(expectedException));
    }

    @Test
    public void shouldLogStartingApp() {
        applicationLogger.startingApplication();
        verify(loggerMock).info(eq("Starting application with settings: " + applicationSettingsMock.toString() + ", jade settings: " + jadeSettingsMock.toString()));
    }

}