/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger;

import org.slf4j.event.Level;
import util.ToStringBuilder;

public class LogWriterNotification {

    private Level level;
    private String message;

    public LogWriterNotification() {
    }

    public LogWriterNotification(Level level, String message) {
        this.level = level;
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("level", level)
                .append("message", message)
                .toString();
    }

}
