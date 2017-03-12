/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationLogger;
import application.ApplicationOption;
import application.ApplicationSettings;
import application.ArgumentType;

public class SettingsOption extends ApplicationOption {

    private ApplicationSettings applicationSettings;
    private ApplicationLogger logger;

    public SettingsOption() {
        logger = new ApplicationLogger(this);
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
        return "Sets application settings, example:\n" +
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
