/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import masoes.jade.settings.JadeSettings;
import org.slf4j.LoggerFactory;

public class JadeOption extends ApplicationOption {

    private JadeSettings jadeSettings;
    private ApplicationLogger logger;

    public JadeOption() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(SettingsOption.class));
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
        return "Sets JADE settings, example:\n" +
                "-Jkey=value -Jkey=value";
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