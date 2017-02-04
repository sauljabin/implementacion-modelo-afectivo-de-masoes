/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test;

public class FunctionalTestException extends RuntimeException {

    public FunctionalTestException(String message) {
        super(message);
    }

    public FunctionalTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionalTestException(Throwable cause) {
        super(cause);
    }

}
