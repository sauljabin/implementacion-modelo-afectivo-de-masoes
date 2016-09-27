/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LogWriterTest {

    private Logger mockLogger;
    private String expectedMessage;
    private Exception expectedException;
    private String expectedFormat;
    private Object[] expectedMessageObjects;
    private String expectedFormatMessage;

    @Before
    public void setUp() throws Exception {
        mockLogger = mock(Logger.class);
        expectedMessage = "message";
        expectedException = new Exception("exception message");
        expectedFormat = "String %s, Integer %d, Double %f";
        expectedMessageObjects = new Object[]{"arg", 1, 0.4};
        expectedFormatMessage = String.format(expectedFormat, expectedMessageObjects);
    }

    @Test
    public void shouldGetNewInstance() {
        LogWriter firstLog = LogWriter.newInstance();
        LogWriter secondLog = LogWriter.newInstance();
        assertThat(firstLog, not(is(secondLog)));
    }

    @Test
    public void shouldInvokeInfoWithMessage() {
        LogWriter.newInstance().message(expectedMessage).info(mockLogger);
        verify(mockLogger).info(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeInfoWithMessageAndError() {
        LogWriter.newInstance().message(expectedMessage).exception(expectedException).info(mockLogger);
        verify(mockLogger).info(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeInfoWithArguments() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).info(mockLogger);
        verify(mockLogger).info(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeInfoWithArgumentsAndError() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).info(mockLogger);
        verify(mockLogger).info(eq(expectedFormatMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeWarnWithMessage() {
        LogWriter.newInstance().message(expectedMessage).warn(mockLogger);
        verify(mockLogger).warn(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeWarnWithMessageAndError() {
        LogWriter.newInstance().message(expectedMessage).exception(expectedException).warn(mockLogger);
        verify(mockLogger).warn(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeWarnWithArguments() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).warn(mockLogger);
        verify(mockLogger).warn(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeWarnWithArgumentsAndError() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).warn(mockLogger);
        verify(mockLogger).warn(eq(expectedFormatMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeErrorWithMessage() {
        LogWriter.newInstance().message(expectedMessage).error(mockLogger);
        verify(mockLogger).error(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeErrorWithMessageAndError() {
        LogWriter.newInstance().message(expectedMessage).exception(expectedException).error(mockLogger);
        verify(mockLogger).error(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeErrorWithArguments() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).error(mockLogger);
        verify(mockLogger).error(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeErrorWithArgumentsAndError() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).error(mockLogger);
        verify(mockLogger).error(eq(expectedFormatMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeDebugWithMessage() {
        LogWriter.newInstance().message(expectedMessage).debug(mockLogger);
        verify(mockLogger).debug(eq(expectedMessage));
    }

    @Test
    public void shouldInvokeDebugWithMessageAndError() {
        LogWriter.newInstance().message(expectedMessage).exception(expectedException).debug(mockLogger);
        verify(mockLogger).debug(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldInvokeDebugWithArguments() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).debug(mockLogger);
        verify(mockLogger).debug(eq(expectedFormatMessage));
    }

    @Test
    public void shouldInvokeDebugWithArgumentsAndError() {
        LogWriter.newInstance().message(expectedFormatMessage).args(expectedMessageObjects).exception(expectedException).debug(mockLogger);
        verify(mockLogger).debug(eq(expectedFormatMessage), eq(expectedException));
    }

}