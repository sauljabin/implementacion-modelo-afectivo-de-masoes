/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

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
    private ApplicationLogger loggerMock;
    private ApplicationOptionProcessor applicationOptionProcessorMock;
    private String[] args;
    private ApplicationBoot applicationBoot;

    @Before
    public void setUp() throws Exception {
        loggerMock = mock(ApplicationLogger.class);
        applicationOptionProcessorMock = mock(ApplicationOptionProcessor.class);
        applicationBoot = new ApplicationBoot();
        setFieldValue(applicationBoot, "logger", loggerMock);
        setFieldValue(applicationBoot, "applicationOptionProcessor", applicationOptionProcessorMock);
        args = new String[]{};
    }

    @Test
    public void shouldInvokeSystemExitFailureWhenException() {
        RuntimeException expectedException = new RuntimeException();
        doThrow(expectedException).when(applicationOptionProcessorMock).processArgs(args);

        expectedSystemExit.checkAssertionAfterwards(() -> verify(loggerMock).cantNotStartApplication(eq(expectedException)));
        expectedSystemExit.expectSystemExitWithStatus(-1);

        applicationBoot.boot(args);
    }

    @Test
    public void shouldInvokeStartApp() {
        applicationBoot.boot(args);
        verify(applicationOptionProcessorMock).processArgs(args);
        verify(loggerMock).startingApplication(any());
        verify(loggerMock).closingApplication();
    }

}