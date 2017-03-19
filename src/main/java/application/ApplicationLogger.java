/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

import jade.JadeSettings;
import logger.LogWriter;
import masoes.MasoesSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import translate.Translation;

import java.util.Arrays;

public class ApplicationLogger {

    private JadeSettings jadeSettings;
    private Logger logger;
    private ApplicationSettings applicationSettings;
    private MasoesSettings masoesSettings;
    private Translation translation;

    public ApplicationLogger(Object object) {
        logger = LoggerFactory.getLogger(object.getClass());
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
        masoesSettings = MasoesSettings.getInstance();
        translation = Translation.getInstance();
    }

    public void startingApplication(String[] args) {
        new LogWriter()
                .message(translation.get("log.starting_application"))
                .args(Arrays.toString(args), applicationSettings.toString(), jadeSettings.toString(), masoesSettings.toString())
                .info(logger);
    }

    public void closingApplication() {
        new LogWriter()
                .message(translation.get("log.closing_application"))
                .info(logger);
    }

    public void cantNotStartApplication(Exception exception) {
        new LogWriter()
                .message(translation.get("log.could_not_start_app"))
                .args(exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void startingOption(ApplicationOption applicationOption) {
        new LogWriter()
                .message(translation.get("log.starting_option"))
                .args(applicationOption)
                .info(logger);
    }

    public void updatedSettings() {
        new LogWriter()
                .message(translation.get("log.update_settings"))
                .args(applicationSettings.toString(), jadeSettings.toString(), masoesSettings.toString())
                .info(logger);
    }

    public void exception(Exception exception) {
        new LogWriter()
                .message(translation.get("log.exception"))
                .args(exception.getMessage())
                .exception(exception)
                .error(logger);
    }

}
