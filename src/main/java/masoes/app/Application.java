/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.setting.SettingsLoader;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Application {

    public static int FAILURE_STATUS = -1;

    private Logger logger;
    private SettingsLoader settingsLoader;
    private ApplicationOptionProcessor cli;
    private Options options;

    public Application(Logger logger, SettingsLoader settingsLoader, ApplicationOptionProcessor cli) {
        this.logger = logger;
        this.settingsLoader = settingsLoader;
        this.cli = cli;
    }

    public Application() {
        logger = LoggerFactory.getLogger(Main.class);
        settingsLoader = SettingsLoader.getInstance();
        options = new Options();
        options.addOption(new HelpOption(options));
        options.addOption(new VersionOption());
        cli = new ApplicationOptionProcessor(options);
    }

    public void run(String[] args) {
        try {
            logger.info("Starting application with arguments: {}, and settings {}", Arrays.toString(args), settingsLoader);
            cli.processArgs(args);
        } catch (Exception e) {
            logger.error("Could not start the application", e);
            System.exit(FAILURE_STATUS);
        }
    }
}
