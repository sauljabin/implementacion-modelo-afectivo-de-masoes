/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

public class AcceptNewAgentException extends RuntimeException {

    public AcceptNewAgentException(String message) {
        super(message);
    }

    public AcceptNewAgentException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcceptNewAgentException(Throwable cause) {
        super(cause);
    }

}
