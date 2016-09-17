/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;

public class ApplicationOptionProcessor {

    private ApplicationOptions options;
    private CommandLineParser commandLineParser;
    private CommandLine commandLine;

    public ApplicationOptionProcessor(ApplicationOptions options) {
        this(options, new DefaultParser());
    }

    public ApplicationOptionProcessor(ApplicationOptions options, CommandLineParser commandLineParser) {
        this.options = options;
        this.commandLineParser = commandLineParser;
    }

    public void processArgs(String[] args) {
        try {
            commandLine = commandLineParser.parse(options, args);
            execOption();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void execOption() {
        for (Option option : options.getOptions()) {
            ApplicationOption applicationOption = (ApplicationOption) option;
            if (commandLine.hasOption(applicationOption.getOpt())) {
                applicationOption.exec(commandLine.getOptionValue(applicationOption.getOpt()));
            }
        }
    }

}
