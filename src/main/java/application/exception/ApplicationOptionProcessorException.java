/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.exception;

public class ApplicationOptionProcessorException extends RuntimeException {

    public ApplicationOptionProcessorException(String message) {
        super(message);
    }

    public ApplicationOptionProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

}
