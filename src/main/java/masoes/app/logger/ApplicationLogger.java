/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.logger;

import jade.core.Agent;
import masoes.app.option.ApplicationOption;
import masoes.app.setting.Setting;
import org.slf4j.Logger;

import java.util.Arrays;

public class ApplicationLogger {

    private Logger logger;

    public ApplicationLogger(Logger logger) {
        this.logger = logger;
    }

    public void startingApplication(String[] args) {
        new LogWriter()
                .message("Starting application with arguments: %s, and settings: %s")
                .args(Arrays.toString(args), Setting.toMap().toString())
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
                .message("Updated settings: %s")
                .args(Setting.toMap().toString())
                .info(logger);
    }

    public void agentException(Agent agent, Exception exception) {
        new LogWriter()
                .message("Exception in agent \"%s\": %s")
                .args(agent.getLocalName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void exception(Exception exception) {
        new LogWriter()
                .message("Exception: %s")
                .args(exception.getMessage())
                .exception(exception)
                .error(logger);
    }

}
