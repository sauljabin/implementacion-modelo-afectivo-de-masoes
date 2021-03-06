/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationOptionProcessor {

    private ApplicationLogger logger;
    private OptionsContainer optionsContainer;
    private CommandLineParser commandLineParser;
    private CommandLine commandLine;

    public ApplicationOptionProcessor() {
        optionsContainer = new OptionsContainer();
        commandLineParser = new DefaultParser();
        logger = new ApplicationLogger(this);
    }

    public void processArgs(String[] args) {
        try {
            commandLine = commandLineParser.parse(optionsContainer.toOptions(), args);
            execOptions(getOptionsToExec());
        } catch (Exception e) {
            throw new ApplicationOptionException(e);
        }
    }

    private void execOptions(List<ApplicationOption> optionsToExec) {
        for (ApplicationOption applicationOption : optionsToExec) {
            execOption(applicationOption);
            if (applicationOption.isFinalOption()) {
                break;
            }
        }
    }

    private List<ApplicationOption> getOptionsToExec() {
        List<ApplicationOption> options = optionsContainer.getApplicationOptionList()
                .stream()
                .filter(tempOption -> commandLine.hasOption(tempOption.getKeyOpt()))
                .collect(Collectors.toList());

        if (isNotPresentOptionDefault(options)) {
            options.add(optionsContainer.getDefaultApplicationOption());
        }

        return options;
    }

    private boolean isNotPresentOptionDefault(List<ApplicationOption> options) {
        return !options.stream().filter(option -> option.getClass().equals(optionsContainer.getApplicationOptionList().getClass())).findFirst().isPresent();
    }

    private void execOption(ApplicationOption option) {
        if (option.getArgType().equals(ArgumentType.ONE_ARG)) {
            option.setValue(commandLine.getOptionValue(option.getKeyOpt()));
        } else if (option.getArgType().equals(ArgumentType.UNLIMITED_ARGS)) {
            option.setProperties(commandLine.getOptionProperties(option.getKeyOpt()));
        }
        logger.startingOption(option);
        option.exec();
    }

}
