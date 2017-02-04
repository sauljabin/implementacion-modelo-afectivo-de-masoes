/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

public class KillContainerException extends RuntimeException {

    public KillContainerException(String message) {
        super(message);
    }

    public KillContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public KillContainerException(Throwable cause) {
        super(cause);
    }

}
