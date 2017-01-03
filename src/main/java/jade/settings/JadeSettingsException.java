/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings;

public class JadeSettingsException extends RuntimeException {

    public JadeSettingsException(String message) {
        super(message);
    }

    public JadeSettingsException(String message, Throwable cause) {
        super(message, cause);
    }

}
