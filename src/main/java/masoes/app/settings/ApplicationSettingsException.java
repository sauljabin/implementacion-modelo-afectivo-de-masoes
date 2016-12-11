/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.settings;

public class ApplicationSettingsException extends RuntimeException {

    public ApplicationSettingsException(String message) {
        super(message);
    }

    public ApplicationSettingsException(String message, Throwable cause) {
        super(message, cause);
    }

}
