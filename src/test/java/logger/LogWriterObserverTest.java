/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

import java.util.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class LogWriterObserverTest {

    private LoggerHandler loggerHandlerMock;
    private LogWriterObserver logWriterObserver;

    @Before
    public void setUp() {
        loggerHandlerMock = mock(LoggerHandler.class);
        logWriterObserver = new LogWriterObserver(loggerHandlerMock);
    }

    @Test
    public void shouldInvokeLoggerHandler() {
        String expectedObject = "expectedObject";
        LogWriterNotification notification = new LogWriterNotification(Level.INFO, expectedObject);
        logWriterObserver.update(mock(Observable.class), notification);
        verify(loggerHandlerMock).handleMessage(Level.INFO, expectedObject);
    }

    @Test
    public void shouldNotInvokeLoggerHandler() {
        logWriterObserver.update(mock(Observable.class), new Object());
        verify(loggerHandlerMock, never()).handleMessage(any(), any());
    }

}