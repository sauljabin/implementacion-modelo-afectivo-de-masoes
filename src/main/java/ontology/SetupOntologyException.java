/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

public class SetupOntologyException extends RuntimeException {

    public SetupOntologyException(String message) {
        super(message);
    }

    public SetupOntologyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SetupOntologyException(Throwable cause) {
        super(cause);
    }

}
