/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.base.ApplicationOption;
import application.base.ArgumentType;
import jade.boot.JadeBoot;

public class BootOption extends ApplicationOption {

    private JadeBoot jadeBoot;

    public BootOption() {
        jadeBoot = new JadeBoot();
    }

    @Override
    public int getOrder() {
        return 60;
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
        return "Starts the application (Default option)";
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
