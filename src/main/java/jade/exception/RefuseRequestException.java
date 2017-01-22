/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.exception;

public class RefuseRequestException extends RuntimeException {

    public RefuseRequestException(String message) {
        super(message);
    }

    public RefuseRequestException(Throwable cause) {
        super(cause);
    }

    public RefuseRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
