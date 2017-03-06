/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LogWriterTest {

    private Logger loggerMock;
    private String expectedMessage;
    private Exception expectedException;
    private String expectedFormat;
    private Object[] expectedMessageObjects;
    private String expectedFormatMessage;
    private LogWriter logWriter;

    @Before
    public void setUp() {
        loggerMock = mock(Logger.class);
        expectedMessage = "message";
        expectedException = new Exception("exception message");
        expectedFormat = "String %s, Integer %d, Double %f";
        expectedMessageObjects = new Object[]{"arg", 1, 0.4};
        expectedFormatMessage = String.format(expectedFormat, expectedMessageObjects);
        logWriter = new LogWriter();
    }

    @Test
    public void shouldInvokeHandlerWithInfo() {
        LoggerHandler handlerMock = mock(LoggerHandler.class);
        logWriter.addLoggerHandler(handlerMock);
        logWriter.message(expectedMessage).info(loggerMock);
        handlerMock.handleMessage(Level.INFO, expectedMessage);
    }

    @Test
    public void shouldInvokeHandlerWithWarn() {
        LoggerHandler handlerMock = mock(LoggerHandler.class);
        logWriter.addLoggerHandler(handlerMock);
        logWriter.message(expectedMessage).warn(loggerMock);
        handlerMock.handleMessage(Level.WARN, expectedMessage);
    }

    @Test
    public void shouldInvokeHandlerWithError() {
        LoggerHandler handlerMock = mock(LoggerHandler.class);
        logWriter.addLoggerHandler(handlerMock);
        logWriter.message(expectedMessage).error(loggerMock);
        handlerMock.handleMessage(Level.ERROR, expectedMessage);
    }

    @Test
    public void shouldInvokeHandlerWithDebug() {
        LoggerHandler handlerMock = mock(LoggerHandler.class);
        logWriter.addLoggerHandler(handlerMock);
        logWriter.message(expectedMessage).debug(loggerMock);
        handlerMock.handleMessage(Level.DEBUG, expectedMessage);
    }

    @Test
    public void shouldInvokeInfoWithMessage() {
        logWriter.message(expectedMessage).info(loggerMock);
        verify(loggerMock).info(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeInfoWithMessageAndError() {
        logWriter.message(expectedMessage).exception(expectedException).info(loggerMock);
        verify(loggerMock).info(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeInfoWithArguments() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).info(loggerMock);
        verify(loggerMock).info(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeInfoWithArgumentsAndError() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).info(loggerMock);
        verify(loggerMock).info(eq(expectedFormatMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeWarnWithMessage() {
        logWriter.message(expectedMessage).warn(loggerMock);
        verify(loggerMock).warn(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeWarnWithMessageAndError() {
        logWriter.message(expectedMessage).exception(expectedException).warn(loggerMock);
        verify(loggerMock).warn(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeWarnWithArguments() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).warn(loggerMock);
        verify(loggerMock).warn(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeWarnWithArgumentsAndError() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).warn(loggerMock);
        verify(loggerMock).warn(eq(expectedFormatMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeErrorWithMessage() {
        logWriter.message(expectedMessage).error(loggerMock);
        verify(loggerMock).error(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeErrorWithMessageAndError() {
        logWriter.message(expectedMessage).exception(expectedException).error(loggerMock);
        verify(loggerMock).error(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeErrorWithArguments() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).error(loggerMock);
        verify(loggerMock).error(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeErrorWithArgumentsAndError() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).error(loggerMock);
        verify(loggerMock).error(eq(expectedFormatMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeDebugWithMessage() {
        logWriter.message(expectedMessage).debug(loggerMock);
        verify(loggerMock).debug(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeDebugWithMessageAndError() {
        logWriter.message(expectedMessage).exception(expectedException).debug(loggerMock);
        verify(loggerMock).debug(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeDebugWithArguments() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).debug(loggerMock);
        verify(loggerMock).debug(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeDebugWithArgumentsAndError() {
        logWriter.message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).debug(loggerMock);
        verify(loggerMock).debug(eq(expectedFormatMessage), eq(expectedException));
    }

    @Test
    public void shouldAddArgsInDifferentInvokes() {
        logWriter.message(expectedFormat).args("arg").args(1).args(0.4).info(loggerMock);
        verify(loggerMock).info(eq(expectedFormatMessage));
    }

}