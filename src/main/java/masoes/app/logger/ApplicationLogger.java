/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import masoes.app.option.ApplicationOption;
import masoes.app.setting.Setting;
import org.slf4j.Logger;

import java.util.Arrays;

public class ApplicationLogger {

    private Logger logger;

    public ApplicationLogger(Logger logger) {
        this.logger = logger;
    }

    private LogWriter newLogWriter() {
        return new LogWriter();
    }

    public void startingApplication(String[] args) {
        newLogWriter()
                .message("Starting application with arguments: %s, and settings: %s")
                .args(Arrays.toString(args), Setting.toMap().toString())
                .info(logger);
    }


    public void cantNotStartApplication(Exception exception) {
        newLogWriter()
                .message("Could not start the application: %s")
                .args(exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void startingOption(ApplicationOption applicationOption) {
        newLogWriter()
                .message("Starting option: %s")
                .args(applicationOption)
                .info(logger);
    }

    public void updatedSettings() {
        newLogWriter()
                .message("Updated settings: %s")
                .args(Setting.toMap().toString())
                .info(logger);
    }

    public void exception(Exception exception) {
        newLogWriter()
                .message("Exception: %s")
                .args(exception.toString())
                .exception(exception)
                .error(logger);
    }

}
