/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

public class JadeBootException extends RuntimeException {

    public JadeBootException(String message) {
        super(message);
    }

    public JadeBootException(String message, Throwable cause) {
        super(message, cause);
    }

    public JadeBootException(Throwable cause) {
        super(cause);
    }

}
