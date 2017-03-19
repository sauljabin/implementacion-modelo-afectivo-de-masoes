/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

import jade.JadeSettings;
import masoes.MasoesSettings;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
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
    private MasoesSettings masoesSettingsMock;
    private HashMap<Object, Object> expectedMasoesSettingsMap;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        jadeSettingsMock = mock(JadeSettings.class);
        masoesSettingsMock = mock(MasoesSettings.class);
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

        expectedMasoesSettingsMap = new HashMap<>();
        expectedMasoesSettingsMap.put("masoes", "value");
        doReturn(expectedMasoesSettingsMap).when(masoesSettingsMock).toMap();
        doCallRealMethod().when(masoesSettingsMock).toString();

        setFieldValue(applicationLogger, "jadeSettings", jadeSettingsMock);
        setFieldValue(applicationLogger, "applicationSettings", applicationSettingsMock);
        setFieldValue(applicationLogger, "masoesSettings", masoesSettingsMock);
        setFieldValue(applicationLogger, "logger", loggerMock);
    }

    @Test
    public void shouldLogStartingAppWithArgs() {
        String[] args = {"-h"};
        applicationLogger.startingApplication(args);
        verify(loggerMock).info(contains("[-h]"));
        verify(loggerMock).info(contains(applicationSettingsMock.toString()));
        verify(loggerMock).info(contains(jadeSettingsMock.toString()));
        verify(loggerMock).info(contains(masoesSettingsMock.toString()));
    }

    @Test
    public void shouldLogClosingApp() {
        applicationLogger.closingApplication();
        verify(loggerMock).info(anyString());
    }

    @Test
    public void shouldLogCantNotStartApp() {
        String message = "message";
        Exception expectedException = new Exception(message);
        applicationLogger.cantNotStartApplication(expectedException);
        verify(loggerMock).error(contains(message), eq(expectedException));
    }

    @Test
    public void shouldLogStartingOption() {
        ApplicationOption applicationOption = mock(ApplicationOption.class);
        String expectedToString = "expectedToString";
        doReturn(expectedToString).when(applicationOption).toString();

        applicationLogger.startingOption(applicationOption);
        verify(loggerMock).info(contains(expectedToString));
    }

    @Test
    public void shouldLogUpdatedSettings() {
        applicationLogger.updatedSettings();
        verify(loggerMock).info(contains(applicationSettingsMock.toString()));
        verify(loggerMock).info(contains(jadeSettingsMock.toString()));
        verify(loggerMock).info(contains(masoesSettingsMock.toString()));
    }

    @Test
    public void shouldLogException() {
        Exception expectedException = new Exception("error");
        applicationLogger.exception(expectedException);
        verify(loggerMock).error(contains(expectedException.getMessage()), eq(expectedException));
    }

}