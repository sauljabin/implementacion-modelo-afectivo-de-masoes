/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

public class LogWriter extends Observable {

    private Optional<String> message;
    private Optional<Exception> exception;
    private List<Object> argsList;

    public LogWriter() {
        init();
    }

    private void init() {
        message = Optional.empty();
        exception = Optional.empty();
        argsList = new ArrayList<>();
    }

    public LogWriter message(String message) {
        this.message = Optional.ofNullable(message);
        return this;
    }

    public LogWriter exception(Exception exception) {
        this.exception = Optional.ofNullable(exception);
        return this;
    }

    public LogWriter args(Object... args) {
        argsList.addAll(Arrays.asList(args));
        return this;
    }

    private String getMessage() {
        if (!argsList.isEmpty()) {
            return String.format(message.orElse("%s"), argsList.toArray());
        } else {
            return message.orElse("");
        }
    }

    public void info(Logger logger) {
        if (exception.isPresent()) {
            logger.info(getMessage(), exception.get());
        } else {
            logger.info(getMessage());
        }
        notifyLog(Level.INFO);
        init();
    }

    public void warn(Logger logger) {
        if (exception.isPresent()) {
            logger.warn(getMessage(), exception.get());
        } else {
            logger.warn(getMessage());
        }
        notifyLog(Level.WARN);
        init();
    }

    public void error(Logger logger) {
        if (exception.isPresent()) {
            logger.error(getMessage(), exception.get());
        } else {
            logger.error(getMessage());
        }
        notifyLog(Level.ERROR);
        init();
    }

    public void debug(Logger logger) {
        if (exception.isPresent()) {
            logger.debug(getMessage(), exception.get());
        } else {
            logger.debug(getMessage());
        }
        notifyLog(Level.DEBUG);
        init();
    }

    public LogWriter addLoggerHandler(LoggerHandler handler) {
        if (handler != null) {
            addObserver(new LogWriterObserver(handler));
        }
        return this;
    }

    private void notifyLog(Level info) {
        setChanged();
        notifyObservers(new LogWriterNotification(info, getMessage()));
    }

}
