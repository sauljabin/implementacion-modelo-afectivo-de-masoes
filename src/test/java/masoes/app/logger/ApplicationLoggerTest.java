/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import jade.core.Agent;
import masoes.app.option.ApplicationOption;
import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Agent.class)
public class ApplicationLoggerTest {

    private Logger mockLogger;
    private ApplicationLogger applicationLogger;
    private SettingsLoader settingsLoader = SettingsLoader.getInstance();

    @Before
    public void setUp() {
        settingsLoader.load();
        mockLogger = mock(Logger.class);
        applicationLogger = new ApplicationLogger(mockLogger);
    }

    @Test
    public void shouldLogStartingApp() {
        String[] args = {"-h"};
        applicationLogger.startingApplication(args);
        verify(mockLogger).info(eq("Starting application with arguments: [-h], and settings: " + Setting.toMap().toString()));
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
        when(applicationOption.toString()).thenReturn(expectedToString);

        applicationLogger.startingOption(applicationOption);
        verify(mockLogger).info(eq("Starting option: " + expectedToString));
    }

    @Test
    public void shouldLogUpdatedSettings() {
        applicationLogger.updatedSettings();
        verify(mockLogger).info(eq("Updated settings: " + Setting.toMap().toString()));
    }

    @Test
    public void shouldLogException() {
        Exception expectedException = new Exception("error");
        applicationLogger.exception(expectedException);
        verify(mockLogger).error(eq("Exception: " + expectedException.getMessage()), eq(expectedException));
    }

    @Test
    public void shouldLogAgentException() {
        Agent mockAgent = mock(Agent.class);
        String expectedAgentName = "agent";
        when(mockAgent.getLocalName()).thenReturn(expectedAgentName);

        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("Exception in agent \"%s\": %s", expectedAgentName, expectedException.getMessage());

        applicationLogger.agentException(mockAgent, expectedException);
        verify(mockLogger).error(eq(expectedMessage), eq(expectedException));
    }

}