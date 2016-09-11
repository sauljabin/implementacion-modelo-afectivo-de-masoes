/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.*;

public class CommandLineInterface {

    private Options options;
    private CommandLine commandLine;
    private Option optionHelp;
    private Option optionVersion;

    public CommandLineInterface() {
        initOptions();
    }

    private void initOptions() {
        options = new Options();
        addOptions();
    }

    private void addOptions() {
        optionHelp = new Option("h", "help", false, "Shows the options");
        optionVersion = new Option("v", "version", false, "Shows the application version");
        options.addOption(optionHelp);
        options.addOption(optionVersion);
    }

    public void processArgs(String[] args) {
        parseArgs(args);
        execOptions();
    }

    private void parseArgs(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasOption(String option) {
        return commandLine.hasOption(option);
    }

    private void execOptions() {
        if (hasOption(optionHelp.getOpt())) {
            execHelp();
        } else if (hasOption(optionVersion.getOpt())) {
            execVersion();
        }
    }

    public void execHelp() {
        try {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Settings.APP_NAME.getValue(), options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execVersion() {
        System.out.println(Settings.APP_NAME.getValue());
        System.out.println(String.format("Version: %s", Settings.APP_VERSION.getValue()));
        System.out.println(String.format("Revision: %s", Settings.APP_REVISION.getValue()));
    }
}
