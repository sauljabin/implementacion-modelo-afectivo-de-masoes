/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.boot;

import application.option.ApplicationOptionProcessor;
import logger.application.ApplicationLogger;
import org.slf4j.LoggerFactory;
import settings.application.ApplicationSettings;
import settings.jade.JadeSettings;

public class ApplicationBoot {

    private static final int STATUS_FAILURE = -1;
    private ApplicationLogger logger;
    private ApplicationSettings applicationSettings;
    private ApplicationOptionProcessor applicationOptionProcessor;
    private JadeSettings jadeSettings;

    public ApplicationBoot() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(ApplicationBoot.class));
        applicationSettings = ApplicationSettings.getInstance();
        applicationOptionProcessor = new ApplicationOptionProcessor();
        jadeSettings = JadeSettings.getInstance();
    }

    public void boot(String[] args) {
        try {
            logger.startingApplication(args);
            applicationOptionProcessor.processArgs(args);
            logger.closingApplication();
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(STATUS_FAILURE);
        }
    }

}
