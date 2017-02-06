/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

public class ExtractOntologyContentException extends RuntimeException {

    public ExtractOntologyContentException(String message) {
        super(message);
    }

    public ExtractOntologyContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtractOntologyContentException(Throwable cause) {
        super(cause);
    }

}
