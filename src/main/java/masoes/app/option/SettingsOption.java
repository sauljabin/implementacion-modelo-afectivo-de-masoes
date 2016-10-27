/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import masoes.app.setting.Setting;
import masoes.util.collection.MapParser;
import org.slf4j.LoggerFactory;

public class SettingsOption extends ApplicationOption {

    private ApplicationLogger applicationLogger;

    public SettingsOption(ApplicationLogger appLogger) {
        applicationLogger = appLogger;
    }

    public SettingsOption() {
        this(new ApplicationLogger(LoggerFactory.getLogger(SettingsOption.class)));
    }

    @Override
    public int getOrder() {
        return 30;
    }

    @Override
    public String getLongOpt() {
        return "settings";
    }

    @Override
    public String getOpt() {
        return "s";
    }

    @Override
    public String getDescription() {
        return "Sets application settings, examples:\n" +
                "-s \"{setting1=value1, setting2=value2}\"\n" +
                "--settings=\"{setting1=value1, setting2=value2}\"";
    }

    @Override
    public boolean hasArg() {
        return true;
    }

    @Override
    public void exec(String optionValue) {
        MapParser mapParser = new MapParser();
        mapParser.parseMap(optionValue).forEach((key, value) -> Setting.set(key, value));
        applicationLogger.updatedSettings();
    }

}
