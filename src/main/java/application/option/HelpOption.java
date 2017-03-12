/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ApplicationSettings;
import application.ArgumentType;
import application.OptionsContainer;
import org.apache.commons.cli.HelpFormatter;

public class HelpOption extends ApplicationOption {

    private static final String LONG_OPT_SEPARATOR = "=";
    private static final String SYNTAX_PREFIX = "Usage: ";
    private ApplicationSettings applicationSettings;
    private HelpFormatter helpFormatter;

    public HelpOption() {
        helpFormatter = new HelpFormatter();
        applicationSettings = ApplicationSettings.getInstance();
    }

    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public String getLongOpt() {
        return "help";
    }

    @Override
    public String getOpt() {
        return "h";
    }

    @Override
    public String getDescription() {
        return "Shows the options";
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
        helpFormatter.setSyntaxPrefix(SYNTAX_PREFIX);
        helpFormatter.setLongOptSeparator(LONG_OPT_SEPARATOR);
        helpFormatter.printHelp(applicationSettings.get(ApplicationSettings.APP_NAME), new OptionsContainer().toOptions());
    }

}
