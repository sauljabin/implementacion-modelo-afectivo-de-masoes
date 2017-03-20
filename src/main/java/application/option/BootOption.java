/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ArgumentType;
import jade.JadeBoot;
import translate.Translation;

public class BootOption extends ApplicationOption {

    private JadeBoot jadeBoot;

    public BootOption() {
        jadeBoot = JadeBoot.getInstance();
    }

    @Override
    public int getOrder() {
        return 70;
    }

    @Override
    public String getLongOpt() {
        return "boot";
    }

    @Override
    public String getOpt() {
        return "b";
    }

    @Override
    public String getDescription() {
        return Translation.getInstance().get("option.boot.description");
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.NO_ARGS;
    }

    @Override
    public boolean isFinalOption() {
        return true;
    }

    @Override
    public void exec() {
        jadeBoot.boot();
    }

}
