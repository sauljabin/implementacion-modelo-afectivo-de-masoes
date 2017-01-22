/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.exception;

public class FailureRequestException extends RuntimeException {

    public FailureRequestException(String message) {
        super(message);
    }

    public FailureRequestException(Throwable cause) {
        super(cause);
    }

    public FailureRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
