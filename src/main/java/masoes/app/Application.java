/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.app.logger.ApplicationLogger;
import masoes.app.option.ApplicationOptionProcessor;
import masoes.app.setting.SettingsLoader;
import masoes.jade.settings.JadeSettings;
import org.slf4j.LoggerFactory;

public class Application {

    private ApplicationLogger logger;
    private SettingsLoader settingsLoader;
    private ApplicationOptionProcessor applicationOptionProcessor;
    private JadeSettings jadeSettings;

    public Application() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(Application.class));
        settingsLoader = SettingsLoader.getInstance();
        applicationOptionProcessor = new ApplicationOptionProcessor();
        jadeSettings = JadeSettings.getInstance();
    }

    public void run(String[] args) {
        try {
            settingsLoader.load();
            jadeSettings.load();
            logger.startingApplication(args);
            applicationOptionProcessor.processArgs(args);
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(SystemExitStatus.FAILURE.getValue());
        }
    }

}
