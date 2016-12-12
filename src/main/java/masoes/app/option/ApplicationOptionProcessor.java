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
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationOptionProcessor {

    private ApplicationLogger logger;
    private ApplicationOptions applicationOptions;
    private CommandLineParser commandLineParser;
    private CommandLine commandLine;

    public ApplicationOptionProcessor() {
        applicationOptions = new ApplicationOptions();
        commandLineParser = new DefaultParser();
        logger = new ApplicationLogger(LoggerFactory.getLogger(ApplicationOptionProcessor.class));
    }

    public void processArgs(String[] args) {
        try {
            commandLine = commandLineParser.parse(applicationOptions.toOptions(), args);
            execOptions(getOptionsToExec());
        } catch (Exception e) {
            throw new OptionProcessorException(e.getMessage(), e);
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
        List<ApplicationOption> options = applicationOptions.getApplicationOptionList()
                .stream()
                .filter(tempOption -> commandLine.hasOption(tempOption.getKeyOpt()))
                .collect(Collectors.toList());
        
        if (isNotPresentOptionDefault(options)) {
            options.add(applicationOptions.getDefaultApplicationOption());
        }

        return options;
    }

    private boolean isNotPresentOptionDefault(List<ApplicationOption> options) {
        return !options.stream().filter(option -> option.getClass().equals(applicationOptions.getApplicationOptionList().getClass())).findFirst().isPresent();
    }

    private void execOption(ApplicationOption option) {
        logger.startingOption(option);
        option.setValue(commandLine.getOptionValue(option.getKeyOpt()));
        option.setProperties(commandLine.getOptionProperties(option.getKeyOpt()));
        option.exec();
    }

}
