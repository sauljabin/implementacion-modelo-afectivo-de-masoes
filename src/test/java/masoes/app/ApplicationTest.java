package masoes.app;

import masoes.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.slf4j.Logger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ApplicationTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    private Logger mockLogger;
    private ApplicationOptionProcessor mockCli;
    private SettingsLoader mockSettings;
    private Application application;
    private String[] args;

    @Before
    public void setUp() {
        mockLogger = mock(Logger.class);
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
        verify(mockLogger).error(any(), eq(toBeThrown));
    }

    @Test
    public void shouldInvokeCli() {
        application.run(args);
        verify(mockCli).processArgs(args);
    }

}