/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.settings.ApplicationSettings;
import org.apache.commons.cli.HelpFormatter;

public class HelpOption extends ApplicationOption {

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
    public void exec() {
        try {
            helpFormatter.setSyntaxPrefix("Usage: ");
            helpFormatter.setLongOptSeparator("=");
            helpFormatter.printHelp(applicationSettings.get(ApplicationSettings.APP_NAME), new ApplicationOptions().toOptions());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
