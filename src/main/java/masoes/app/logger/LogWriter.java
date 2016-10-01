/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import org.slf4j.Logger;

import java.util.Optional;

public class LogWriter {

    private Optional<String> message;
    private Optional<Exception> exception;
    private Optional<Object[]> args;

    private LogWriter() {
        message = Optional.empty();
        exception = Optional.empty();
        args = Optional.empty();
    }

    public static LogWriter newInstance() {
        return new LogWriter();
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
        this.args = Optional.ofNullable(args);
        return this;
    }

    private String getMessage() {
        if (args.isPresent()) {
            return String.format(message.orElse("%s"), args.get());
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
    }

    public void warn(Logger logger) {
        if (exception.isPresent()) {
            logger.warn(getMessage(), exception.get());
        } else {
            logger.warn(getMessage());
        }
    }

    public void error(Logger logger) {
        if (exception.isPresent()) {
            logger.error(getMessage(), exception.get());
        } else {
            logger.error(getMessage());
        }
    }

    public void debug(Logger logger) {
        if (exception.isPresent()) {
            logger.debug(getMessage(), exception.get());
        } else {
            logger.debug(getMessage());
        }
    }

}
