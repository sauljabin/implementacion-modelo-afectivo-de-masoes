/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

public class ApplicationOptionException extends RuntimeException {

    public ApplicationOptionException(String message) {
        super(message);
    }

    public ApplicationOptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationOptionException(Throwable cause) {
        super(cause);
    }

}
