/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.app.logger.ApplicationLogger;
import masoes.app.option.ApplicationOptionProcessor;
import masoes.app.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ApplicationTest {

    @Rule
    public final ExpectedSystemExit expectedSystemExit = ExpectedSystemExit.none();
    private ApplicationLogger mockLogger;
    private ApplicationOptionProcessor mockCli;
    private SettingsLoader mockSettings;
    private String[] args;
    private Application application;

    @Before
    public void setUp() {
        mockLogger = mock(ApplicationLogger.class);
        mockSettings = mock(SettingsLoader.class);
        mockCli = mock(ApplicationOptionProcessor.class);

        application = new Application(mockLogger, mockSettings, mockCli);
        args = new String[]{};
    }

    @Test
    public void shouldInvokeSystemExitWhenException() {
        RuntimeException expectedException = new RuntimeException();
        doThrow(expectedException).when(mockCli).processArgs(args);

        expectedSystemExit.checkAssertionAfterwards(() -> verify(mockLogger).cantNotStartApplication(eq(expectedException)));
        expectedSystemExit.expectSystemExitWithStatus(-1);

        application.run(args);
    }

    @Test
    public void shouldStartApp() {
        application.run(args);

        verify(mockCli).processArgs(args);
        verify(mockLogger).startingApplication(any());
        verify(mockSettings).load();
    }

}