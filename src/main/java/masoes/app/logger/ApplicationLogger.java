/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import masoes.app.option.ApplicationOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

public class ApplicationLogger {

    private Logger logger;

    private ApplicationLogger(Logger logger) {
        this.logger = logger;
    }

    public static ApplicationLogger newInstance(Class<?> classObject) {
        return new ApplicationLogger(LoggerFactory.getLogger(classObject));
    }

    public void startingApplication(String[] args, Map<String, String> settings) {
        LogWriter.newInstance()
                .message("Starting application with arguments: %s, and settings %s")
                .args(Arrays.toString(args), settings.toString())
                .info(logger);
    }

    public void cantNotStartApplication(Exception exception) {
        LogWriter.newInstance()
                .message("Could not start the application -> %s")
                .args(exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void startingOption(ApplicationOption applicationOption) {
        LogWriter.newInstance()
                .message("Starting option: %s")
                .args(applicationOption)
                .info(logger);
    }

    public void updatedSettings(Map<String, String> settings) {
        LogWriter.newInstance()
                .message("Updated settings: %s")
                .args(settings.toString())
                .info(logger);
    }

}
