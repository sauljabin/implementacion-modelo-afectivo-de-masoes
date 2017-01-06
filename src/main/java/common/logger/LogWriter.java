/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package common.logger;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LogWriter {

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
        init();
    }

    public void warn(Logger logger) {
        if (exception.isPresent()) {
            logger.warn(getMessage(), exception.get());
        } else {
            logger.warn(getMessage());
        }
        init();
    }

    public void error(Logger logger) {
        if (exception.isPresent()) {
            logger.error(getMessage(), exception.get());
        } else {
            logger.error(getMessage());
        }
        init();
    }

    public void debug(Logger logger) {
        if (exception.isPresent()) {
            logger.debug(getMessage(), exception.get());
        } else {
            logger.debug(getMessage());
        }
        init();
    }

}
