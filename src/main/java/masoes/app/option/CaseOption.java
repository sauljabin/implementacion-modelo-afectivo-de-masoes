/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.setting.Setting;

public class CaseOption extends ApplicationOption {

    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public String getOpt() {
        return "c";
    }

    @Override
    public String getLongOpt() {
        return "case";
    }

    @Override
    public String getDescription() {
        return "Sets the case study";
    }

    @Override
    public boolean hasArg() {
        return true;
    }

    @Override
    public void exec(String optionValue) {
        Setting.MASOES_CASE.setValue(optionValue);
    }
}
