/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

public class FillOntologyContentException extends RuntimeException {

    public FillOntologyContentException(String message) {
        super(message);
    }

    public FillOntologyContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public FillOntologyContentException(Throwable cause) {
        super(cause);
    }

}
