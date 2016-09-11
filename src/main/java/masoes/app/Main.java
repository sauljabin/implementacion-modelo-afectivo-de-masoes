/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {

    private static int FAILURE_STATUS = -1;
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static SettingsLoader settingsLoader = SettingsLoader.getInstance();
    private static CommandLineInterface cli = new CommandLineInterface();

    public static void main(String[] args) {
        try {
            logger.info("Starting application with arguments: %s, and settings %s", Arrays.toString(args), settingsLoader);
            cli.processArgs(args);
        } catch (Exception e) {
            logger.error("Could not start the application", e);
            System.exit(FAILURE_STATUS);
        }

    }
}
