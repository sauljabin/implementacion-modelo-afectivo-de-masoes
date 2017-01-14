/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.boot;

import application.logger.ApplicationLogger;
import application.option.ApplicationOptionProcessor;
import application.settings.ApplicationSettings;
import jade.settings.JadeSettings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ApplicationBootTest {

    @Rule
    public ExpectedSystemExit expectedSystemExit = ExpectedSystemExit.none();
    private ApplicationLogger mockLogger;
    private ApplicationOptionProcessor mockApplicationOptionProcessor;
    private ApplicationSettings mockApplicationSettings;
    private String[] args;
    private ApplicationBoot applicationBoot;
    private JadeSettings mockJadeSettings;

    @Before
    public void setUp() throws Exception {
        mockJadeSettings = mock(JadeSettings.class);
        mockLogger = mock(ApplicationLogger.class);
        mockApplicationSettings = mock(ApplicationSettings.class);
        mockApplicationOptionProcessor = mock(ApplicationOptionProcessor.class);
        applicationBoot = new ApplicationBoot();
        setFieldValue(applicationBoot, "logger", mockLogger);
        setFieldValue(applicationBoot, "applicationSettings", mockApplicationSettings);
        setFieldValue(applicationBoot, "applicationOptionProcessor", mockApplicationOptionProcessor);
        setFieldValue(applicationBoot, "jadeSettings", mockJadeSettings);
        args = new String[]{};
    }

    @Test
    public void shouldInvokeSystemExitWhenException() {
        RuntimeException expectedException = new RuntimeException();
        doThrow(expectedException).when(mockApplicationOptionProcessor).processArgs(args);

        expectedSystemExit.checkAssertionAfterwards(() -> verify(mockLogger).cantNotStartApplication(eq(expectedException)));
        expectedSystemExit.expectSystemExitWithStatus(-1);

        applicationBoot.boot(args);
    }

    @Test
    public void shouldStartApp() {
        applicationBoot.boot(args);
        verify(mockApplicationOptionProcessor).processArgs(args);
        verify(mockLogger).startingApplication(any());
        verify(mockLogger).closingApplication();
    }

}