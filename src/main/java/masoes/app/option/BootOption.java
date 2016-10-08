/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.env.EnvironmentFactory;

public class BootOption extends ApplicationOption {

    private EnvironmentFactory environmentFactory;

    public BootOption(EnvironmentFactory environmentFactory) {
        this.environmentFactory = environmentFactory;
    }

    public BootOption() {
        this(new EnvironmentFactory());
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
        return "Starts the application";
    }

    @Override
    public boolean hasArg() {
        return false;
    }

    @Override
    public void exec(String optionValue) {
        environmentFactory.createEnvironment();
    }
}
