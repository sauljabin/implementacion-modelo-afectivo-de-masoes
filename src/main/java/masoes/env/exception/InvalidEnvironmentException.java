/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.exception;

public class InvalidEnvironmentException extends RuntimeException {

    public InvalidEnvironmentException(String message) {
        super(message);
    }

    public InvalidEnvironmentException(String message, Throwable cause) {
        super(message, cause);
    }

}
