/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

public class AgentOperationException extends RuntimeException {

    public AgentOperationException(String message) {
        super(message);
    }

    public AgentOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentOperationException(Throwable cause) {
        super(cause);
    }

}
