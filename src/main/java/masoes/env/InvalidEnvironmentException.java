/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import java.security.InvalidParameterException;

public class InvalidEnvironmentException extends InvalidParameterException {

    public InvalidEnvironmentException(String message) {
        super(message);
    }

}
