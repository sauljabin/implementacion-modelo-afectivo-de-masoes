/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.logger;

import org.slf4j.Logger;

public class LogWriter {

    private String message;
    private Exception exception;
    private Object[] args;

    private LogWriter() {

    }

    public static LogWriter newInstance() {
        return new LogWriter();
    }

    public LogWriter message(String message) {
        this.message = message;
        return this;
    }

    public LogWriter exception(Exception exception) {
        this.exception = exception;
        return this;
    }

    public LogWriter args(Object... args) {
        this.args = args;
        return this;
    }

    private String getMessage() {
        if (args == null) {
            return message;
        } else {
            return String.format(message, args);
        }
    }

    public void info(Logger logger) {
        if (exception == null) {
            logger.info(getMessage());
        } else {
            logger.info(getMessage(), exception);
        }
    }

    public void warn(Logger logger) {
        if (exception == null) {
            logger.warn(getMessage());
        } else {
            logger.warn(getMessage(), exception);
        }
    }

    public void error(Logger logger) {
        if (exception == null) {
            logger.error(getMessage());
        } else {
            logger.error(getMessage(), exception);
        }
    }

    public void debug(Logger logger) {
        if (exception == null) {
            logger.debug(getMessage());
        } else {
            logger.debug(getMessage(), exception);
        }
    }
}
