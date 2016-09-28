/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class ApplicationOptionProcessor {

    private ApplicationLogger logger;
    private ApplicationOptionManager options;
    private CommandLineParser commandLineParser;
    private CommandLine commandLine;

    public ApplicationOptionProcessor(ApplicationOptionManager options) {
        this(options, new DefaultParser(), ApplicationLogger.newInstance(ApplicationOptionProcessor.class));
    }

    public ApplicationOptionProcessor(ApplicationOptionManager options, CommandLineParser commandLineParser, ApplicationLogger logger) {
        this.options = options;
        this.commandLineParser = commandLineParser;
        this.logger = logger;
    }

    public void processArgs(String[] args) {
        try {
            commandLine = commandLineParser.parse(options.toOptions(), args);
            execOption();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void execOption() {
        ApplicationOption option = options.getApplicationOptions()
                .stream()
                .filter(tempOption -> commandLine.hasOption(tempOption.getKeyOpt()))
                .findFirst()
                .orElse(options.getDefaultOption());
        logger.startingOption(option);
        option.exec(commandLine.getOptionValue(option.getKeyOpt()));
    }

}
