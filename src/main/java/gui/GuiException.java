/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

public class GuiException extends RuntimeException {

    public GuiException(String message) {
        super(message);
    }

    public GuiException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuiException(Throwable cause) {
        super(cause);
    }

}
