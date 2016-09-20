/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.app.logger.ApplicationLogger;
import masoes.app.option.ApplicationOptionProcessor;
import masoes.app.option.OptionsCollection;
import masoes.app.setting.SettingsLoader;

public class Application {

    public static int FAILURE_STATUS = -1;

    private ApplicationLogger logger;
    private SettingsLoader settingsLoader;
    private ApplicationOptionProcessor applicationOptionProcessor;
    private OptionsCollection optionsCollection;

    public Application(ApplicationLogger logger, SettingsLoader settingsLoader, ApplicationOptionProcessor applicationOptionProcessor) {
        this.logger = logger;
        this.settingsLoader = settingsLoader;
        this.applicationOptionProcessor = applicationOptionProcessor;
    }

    public Application() {
        logger = ApplicationLogger.newInstance(Main.class);
        settingsLoader = SettingsLoader.getInstance();
        optionsCollection = OptionsCollection.getInstance();
        applicationOptionProcessor = new ApplicationOptionProcessor(optionsCollection);
    }

    public void run(String[] args) {
        try {
            logger.startingApplication(args, settingsLoader.toMap());
            applicationOptionProcessor.processArgs(args);
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(FAILURE_STATUS);
        }
    }
}
