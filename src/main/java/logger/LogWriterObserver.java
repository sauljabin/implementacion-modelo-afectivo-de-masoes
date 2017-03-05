/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger;

import java.util.Observable;
import java.util.Observer;

public class LogWriterObserver implements Observer {

    private LoggerHandler loggerHandler;

    public LogWriterObserver(LoggerHandler loggerHandler) {
        this.loggerHandler = loggerHandler;
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (arg instanceof LogWriterNotification) {
            LogWriterNotification notification = (LogWriterNotification) arg;
            loggerHandler.handleMessage(notification.getLevel(), notification.getMessage());
        }
    }

}
