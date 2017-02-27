/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

public class BehaviouralComponentException extends RuntimeException {

    public BehaviouralComponentException(String message) {
        super(message);
    }

    public BehaviouralComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public BehaviouralComponentException(Throwable cause) {
        super(cause);
    }

}
