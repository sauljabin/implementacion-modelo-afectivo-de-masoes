/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol.exception;

public class RequesterException extends RuntimeException {

    public RequesterException(String message) {
        super(message);
    }

    public RequesterException(String message, Throwable cause) {
        super(message, cause);
    }

}