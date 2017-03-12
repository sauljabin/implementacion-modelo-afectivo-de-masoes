/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

public class ApplicationBoot {

    private static final int STATUS_FAILURE = -1;
    private ApplicationLogger logger;
    private ApplicationOptionProcessor applicationOptionProcessor;

    public ApplicationBoot() {
        logger = new ApplicationLogger(this);
        applicationOptionProcessor = new ApplicationOptionProcessor();
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
