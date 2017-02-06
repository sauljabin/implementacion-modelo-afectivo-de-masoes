/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

public class TimeoutRequestException extends RuntimeException {

    public TimeoutRequestException(String message) {
        super(message);
    }

    public TimeoutRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutRequestException(Throwable cause) {
        super(cause);
    }

}
