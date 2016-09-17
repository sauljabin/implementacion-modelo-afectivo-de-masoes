/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.logger.ApplicationLogger;
import masoes.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ApplicationTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    private ApplicationLogger mockLogger;
    private ApplicationOptionProcessor mockCli;
    private SettingsLoader mockSettings;
    private Application application;
    private String[] args;

    @Before
    public void setUp() {
        mockLogger = mock(ApplicationLogger.class);
        mockCli = mock(ApplicationOptionProcessor.class);
        mockSettings = mock(SettingsLoader.class);
        application = new Application(mockLogger, mockSettings, mockCli);
        args = new String[]{};
    }

    @Test
    public void shouldInvokeLoggerErrorWhenException() {
        RuntimeException toBeThrown = new RuntimeException();
        doThrow(toBeThrown).when(mockCli).processArgs(args);
        exit.expectSystemExitWithStatus(Application.FAILURE_STATUS);
        application.run(args);
        verify(mockLogger).cantNotStartApplication(eq(toBeThrown));
    }

    @Test
    public void shouldStartApp() {
        application.run(args);
        verify(mockCli).processArgs(args);
        verify(mockLogger).startingApplication(any(), anyMap());
    }

}