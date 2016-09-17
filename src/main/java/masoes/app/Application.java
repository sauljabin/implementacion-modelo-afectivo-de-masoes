/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.logger.ApplicationLogger;
import masoes.setting.SettingsLoader;
import org.apache.commons.cli.Options;

public class Application {

    public static int FAILURE_STATUS = -1;

    private ApplicationLogger logger;
    private SettingsLoader settingsLoader;
    private ApplicationOptionProcessor cli;

    public Application(ApplicationLogger logger, SettingsLoader settingsLoader, ApplicationOptionProcessor cli) {
        this.logger = logger;
        this.settingsLoader = settingsLoader;
        this.cli = cli;
    }

    public Application() {
        logger = ApplicationLogger.getInstance(Main.class);
        settingsLoader = SettingsLoader.getInstance();
        cli = initApplicationOptionProcessor();
    }

    private ApplicationOptionProcessor initApplicationOptionProcessor() {
        Options options = new Options();
        options.addOption(new HelpOption(options));
        options.addOption(new VersionOption());
        return new ApplicationOptionProcessor(options);
    }

    public void run(String[] args) {
        try {
            logger.startingApplication(args, settingsLoader.toMap());
            cli.processArgs(args);
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(FAILURE_STATUS);
        }
    }
}
