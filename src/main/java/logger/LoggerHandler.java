/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger;

import org.slf4j.event.Level;

public interface LoggerHandler {

    void handleMessage(Level level, String message);

}
