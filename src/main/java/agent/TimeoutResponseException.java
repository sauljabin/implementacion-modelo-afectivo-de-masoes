/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

public class TimeoutResponseException extends RuntimeException {

    public TimeoutResponseException(String message) {
        super(message);
    }

    public TimeoutResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutResponseException(Throwable cause) {
        super(cause);
    }

}
