/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationLogger;
import application.ApplicationOption;
import application.ArgumentType;
import jade.JadeSettings;
import translate.Translation;

public class JadeOption extends ApplicationOption {

    private JadeSettings jadeSettings;
    private ApplicationLogger logger;

    public JadeOption() {
        logger = new ApplicationLogger(this);
        jadeSettings = JadeSettings.getInstance();
    }

    @Override
    public int getOrder() {
        return 40;
    }

    @Override
    public String getLongOpt() {
        return null;
    }

    @Override
    public String getOpt() {
        return "J";
    }

    @Override
    public String getDescription() {
        return Translation.getInstance().get("option.jade.description");
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
        getProperties().entrySet().forEach(objectEntry -> jadeSettings.set(objectEntry.getKey().toString(), objectEntry.getValue().toString()));
        logger.updatedSettings();
    }

}
