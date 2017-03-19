/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationLogger;
import application.ApplicationOption;
import application.ArgumentType;
import masoes.MasoesSettings;
import translate.Translation;

public class MasoesOption extends ApplicationOption {

    private MasoesSettings masoesSettings;
    private ApplicationLogger logger;

    public MasoesOption() {
        logger = new ApplicationLogger(this);
        masoesSettings = MasoesSettings.getInstance();
    }

    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public String getLongOpt() {
        return null;
    }

    @Override
    public String getOpt() {
        return "M";
    }

    @Override
    public String getDescription() {
        return Translation.getInstance().get("option.masoes.description");
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
        getProperties().entrySet().forEach(objectEntry -> masoesSettings.set(objectEntry.getKey().toString(), objectEntry.getValue().toString()));
        logger.updatedSettings();
    }

}
