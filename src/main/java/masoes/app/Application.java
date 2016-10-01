/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.app.logger.ApplicationLogger;
import masoes.app.option.ApplicationOptionManager;
import masoes.app.option.ApplicationOptionProcessor;
import masoes.app.setting.SettingsLoader;

public class Application {

    public static int FAILURE_STATUS = -1;

    private ApplicationLogger logger;
    private SettingsLoader settingsLoader;
    private ApplicationOptionProcessor applicationOptionProcessor;

    public Application() {
        this(ApplicationLogger.newInstance(Application.class), SettingsLoader.getInstance(), new ApplicationOptionProcessor(ApplicationOptionManager.getInstance()));
    }

    public Application(ApplicationLogger logger, SettingsLoader settingsLoader, ApplicationOptionProcessor applicationOptionProcessor) {
        this.logger = logger;
        this.settingsLoader = settingsLoader;
        this.applicationOptionProcessor = applicationOptionProcessor;
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
