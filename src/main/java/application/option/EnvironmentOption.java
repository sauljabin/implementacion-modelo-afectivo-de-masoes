/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ApplicationSettings;
import application.ArgumentType;
import environment.Environment;
import environment.EnvironmentFactory;
import jade.JadeSettings;

public class EnvironmentOption extends ApplicationOption {

    private JadeSettings jadeSettings;
    private EnvironmentFactory environmentFactory;
    private ApplicationSettings applicationSettings;

    public EnvironmentOption() {
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
        environmentFactory = new EnvironmentFactory();
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
        return "E";
    }

    @Override
    public String getDescription() {
        return "Sets the environment (dummy, wikipedia), example:\n" +
                "-Edummy";
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.ONE_ARG;
    }

    @Override
    public boolean isFinalOption() {
        return false;
    }

    @Override
    public void exec() {
        applicationSettings.set(ApplicationSettings.MASOES_ENV, getValue());
        Environment environment = environmentFactory.createEnvironment(getValue());
        jadeSettings.set(JadeSettings.AGENTS, environment.toJadeParameter());
    }

}
