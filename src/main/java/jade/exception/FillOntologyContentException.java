/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.exception;

public class FillOntologyContentException extends RuntimeException {

    public FillOntologyContentException(String message) {
        super(message);
    }

    public FillOntologyContentException(String message, Throwable cause) {
        super(message, cause);
    }

}
