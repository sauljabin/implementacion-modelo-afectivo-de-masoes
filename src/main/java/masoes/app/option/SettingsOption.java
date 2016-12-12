/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import masoes.app.settings.ApplicationSettings;
import org.slf4j.LoggerFactory;

public class SettingsOption extends ApplicationOption {

    private ApplicationSettings applicationSettings;
    private ApplicationLogger logger;

    public SettingsOption() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(SettingsOption.class));
        applicationSettings = ApplicationSettings.getInstance();
    }

    @Override
    public int getOrder() {
        return 30;
    }

    @Override
    public String getLongOpt() {
        return null;
    }

    @Override
    public String getOpt() {
        return "S";
    }

    @Override
    public String getDescription() {
        return "Sets application settings, examples:\n" +
                "-Skey=value -Skey=value";
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.UNLIMITED_ARGS;
    }

    @Override
    public boolean isFinalOption() {
        return false;
    }

    @Override
    public void exec() {
        getProperties().entrySet().forEach(objectEntry -> applicationSettings.set(objectEntry.getKey().toString(), objectEntry.getValue().toString()));
        logger.updatedSettings();
    }

}
