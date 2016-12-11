/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.setting.Setting;

public class EnvironmentOption extends ApplicationOption {

    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public String getLongOpt() {
        return "env";
    }

    @Override
    public String getOpt() {
        return "e";
    }

    @Override
    public String getDescription() {
        return "Sets the environment for case study";
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.ONE_ARG;
    }

    @Override
    public void exec() {
        Setting.MASOES_ENV.setValue(getValue());
    }

}
