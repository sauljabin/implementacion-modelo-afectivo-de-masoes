/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.ontology;

public class SetupOntologyException extends RuntimeException {

    public SetupOntologyException(String message) {
        super(message);
    }

    public SetupOntologyException(String message, Throwable cause) {
        super(message, cause);
    }

}
