/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ArgumentType;
import environment.Environment;
import environment.EnvironmentFactory;
import jade.JadeSettings;
import masoes.MasoesSettings;
import translate.Translation;

public class EnvironmentOption extends ApplicationOption {

    private JadeSettings jadeSettings;
    private EnvironmentFactory environmentFactory;
    private MasoesSettings masoesSettings;

    public EnvironmentOption() {
        masoesSettings = MasoesSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
        environmentFactory = new EnvironmentFactory();
    }

    @Override
    public int getOrder() {
        return 60;
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
        return Translation.getInstance().get("option.environment.description");
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
        masoesSettings.set(MasoesSettings.MASOES_ENV, getValue());
        Environment environment = environmentFactory.createEnvironment(getValue());
        jadeSettings.set(JadeSettings.AGENTS, environment.toJadeParameter());
    }

}
