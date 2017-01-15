/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.logger;

import application.option.ApplicationOption;
import application.settings.ApplicationSettings;
import common.logger.LogWriter;
import jade.settings.JadeSettings;
import org.slf4j.Logger;

import java.util.Arrays;

public class ApplicationLogger {

    private JadeSettings jadeSettings;
    private Logger logger;
    private ApplicationSettings applicationSettings;

    public ApplicationLogger(Logger logger) {
        this.logger = logger;
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
    }

    public void startingApplication(String[] args) {
        new LogWriter()
                .message("Starting application with arguments: %s, settings: %s, jade settings: %s")
                .args(Arrays.toString(args), applicationSettings.toString(), jadeSettings.toString())
                .info(logger);
    }

    public void closingApplication() {
        new LogWriter()
                .message("Closing application")
                .info(logger);
    }

    public void cantNotStartApplication(Exception exception) {
        new LogWriter()
                .message("Could not start the application: %s")
                .args(exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void startingOption(ApplicationOption applicationOption) {
        new LogWriter()
                .message("Starting option: %s")
                .args(applicationOption)
                .info(logger);
    }

    public void updatedSettings() {
        new LogWriter()
                .message("Updated settings: %s, jade settings: %s")
                .args(applicationSettings.toString(), jadeSettings.toString())
                .info(logger);
    }

    public void exception(Exception exception) {
        new LogWriter()
                .message("Exception: %s")
                .args(exception.getMessage())
                .exception(exception)
                .error(logger);
    }

}