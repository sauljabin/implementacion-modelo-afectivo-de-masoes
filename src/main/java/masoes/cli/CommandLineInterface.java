/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.cli;

import masoes.app.Settings;
import org.apache.commons.cli.*;

public class CommandLineInterface {

    private Options options;
    private CommandLine commandLine;
    private Option optionHelp;

    public CommandLineInterface() {
        initOptions();
    }

    private void initOptions() {
        optionHelp = new Option("h", "help", false, "Shows the options");
        options = new Options();
        options.addOption(optionHelp);
    }

    public void processArgs(String[] args) {
        try {
            args = setDefaultArgs(args);
            CommandLineParser parser = new DefaultParser();
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] setDefaultArgs(String[] args) {
        if (args.length == 0)
            args = new String[]{"-" + optionHelp.getOpt()};
        return args;
    }

    public boolean hasOption(String option) {
        return commandLine.hasOption(option);
    }

    public void execHelp() {
        try {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Settings.getInstance().get(Settings.APP_NAME_KEY, "appName"), options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execOptions() {
        execHelp();
    }
}
