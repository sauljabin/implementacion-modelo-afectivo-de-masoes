/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.app.logger.ApplicationLogger;
import masoes.app.option.ApplicationOptionProcessor;
import masoes.app.setting.SettingsLoader;
import org.slf4j.LoggerFactory;

public class Application {

    private ApplicationLogger logger;
    private SettingsLoader settingsLoader;
    private ApplicationOptionProcessor applicationOptionProcessor;

    public Application() {
        this(new ApplicationLogger(LoggerFactory.getLogger(Application.class)), SettingsLoader.getInstance(), new ApplicationOptionProcessor());
    }

    public Application(ApplicationLogger logger, SettingsLoader settingsLoader, ApplicationOptionProcessor applicationOptionProcessor) {
        this.logger = logger;
        this.settingsLoader = settingsLoader;
        this.applicationOptionProcessor = applicationOptionProcessor;
    }

    public void run(String[] args) {
        try {
            settingsLoader.load();
            logger.startingApplication(args);
            applicationOptionProcessor.processArgs(args);
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(SystemExitStatus.FAILURE.getValue());
        }
    }

}
