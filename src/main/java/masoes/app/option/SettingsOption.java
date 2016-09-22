/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import masoes.app.setting.Setting;

import java.security.InvalidParameterException;

public class SettingsOption extends ApplicationOption {

    private ApplicationLogger applicationLogger;

    public SettingsOption(ApplicationLogger appLogger) {
        applicationLogger = appLogger;
    }

    public SettingsOption() {
        applicationLogger = ApplicationLogger.newInstance(SettingsOption.class);
    }

    @Override
    public int getOrder() {
        return 30;
    }

    @Override
    public String getOpt() {
        return "s";
    }

    @Override
    public String getLongOpt() {
        return "settings";
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
        validateArgument(optionValue);
        String[] settings = splitArgument(optionValue);
        for (String pair : settings) {
            String[] pairArray = pair.split("=");
            Setting.set(pairArray[0].trim(), pairArray[1].trim());
        }
        applicationLogger.updatedSettings(Setting.toMap());
    }

    private void validateArgument(String optionValue) {
        if (!optionValue.matches("^\\{(\\w+=\\w+(, *)?)+\\}$")) {
            throw new InvalidParameterException("Incorrect settings format: " + optionValue);
        }
    }

    private String[] splitArgument(String optionValue) {
        return optionValue.trim().replaceAll("\\{|\\}", "").trim().split(",");
    }

}
