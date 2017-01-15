/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.logger.ApplicationLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ApplicationOptionProcessorTest {

    private ApplicationOptionProcessor applicationOptionProcessor;
    private ApplicationOptions mockApplicationOptions;
    private String expectedOpt;
    private String[] expectedArgs;
    private List<ApplicationOption> applicationOptionList;

    private CommandLineParser mockCommandLineParser;
    private CommandLine mockCommandLine;
    private ApplicationLogger mockLogger;
    private ApplicationOption mockOption;
    private ApplicationOption mockDefaultOption;
    private String defaultKeyOption;

    @Before
    public void setUp() throws Exception {
        mockCommandLineParser = mock(DefaultParser.class);
        mockCommandLine = mock(CommandLine.class);
        mockLogger = mock(ApplicationLogger.class);
        mockApplicationOptions = mock(ApplicationOptions.class);

        applicationOptionProcessor = new ApplicationOptionProcessor();
        setFieldValue(applicationOptionProcessor, "applicationOptions", mockApplicationOptions);
        setFieldValue(applicationOptionProcessor, "commandLineParser", mockCommandLineParser);
        setFieldValue(applicationOptionProcessor, "logger", mockLogger);

        expectedOpt = "test";
        mockOption = getCreateMockApplicationOption(expectedOpt, false, ArgumentType.ONE_ARG);

        defaultKeyOption = "default";
        mockDefaultOption = getCreateMockApplicationOption(defaultKeyOption, false, ArgumentType.NO_ARGS);

        applicationOptionList = new ArrayList<>();
        applicationOptionList.add(mockOption);

        doReturn(applicationOptionList).when(mockApplicationOptions).getApplicationOptionList();
        doReturn(mockCommandLine).when(mockCommandLineParser).parse(any(), any());
        doReturn(Boolean.TRUE).when(mockCommandLine).hasOption(expectedOpt);
        doReturn(mockDefaultOption).when(mockApplicationOptions).getDefaultApplicationOption();

        expectedArgs = new String[]{"-" + expectedOpt};
    }

    @Test
    public void shouldParseArgs() throws Exception {
        Options expectedOptions = new Options();
        doReturn(expectedOptions).when(mockApplicationOptions).toOptions();

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockCommandLineParser).parse(eq(expectedOptions), eq(expectedArgs));
    }

    @Test
    public void shouldInvokeOptionAndSeValue() {
        String expectedOptionValue = "value";
        doReturn(expectedOptionValue).when(mockCommandLine).getOptionValue(any());

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption).setValue(expectedOptionValue);
        verify(mockOption).exec();
        verify(mockLogger).startingOption(mockOption);
    }

    @Test
    public void shouldInvokeOptionAndSetProperties() {
        Properties mockProperties = mock(Properties.class);
        doReturn(ArgumentType.UNLIMITED_ARGS).when(mockOption).getArgType();
        doReturn(mockProperties).when(mockCommandLine).getOptionProperties(any());

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption).setProperties(mockProperties);
        verify(mockOption).exec();
        verify(mockLogger).startingOption(mockOption);
    }

    @Test
    public void shouldInvokeTwoOptions() {
        String expectedOpt2 = "test";
        ApplicationOption mockOption2 = getCreateMockApplicationOption(expectedOpt2, false, ArgumentType.NO_ARGS);
        applicationOptionList.add(mockOption2);
        doReturn(Boolean.TRUE).when(mockCommandLine).hasOption(expectedOpt2);

        expectedArgs = new String[]{"-" + expectedOpt, "-" + expectedOpt2};

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockLogger).startingOption(mockOption);
        verify(mockLogger).startingOption(mockOption2);
        verify(mockOption).exec();
        verify(mockOption2).exec();
    }

    @Test
    public void shouldBreakWheOptionIsFinalOption() {
        applicationOptionList = new ArrayList<>();
        doReturn(applicationOptionList).when(mockApplicationOptions).getApplicationOptionList();

        String expectedOpt1 = "a";
        ApplicationOption mockOption1 = getCreateMockApplicationOption(expectedOpt1, true, ArgumentType.NO_ARGS);
        doReturn(Boolean.TRUE).when(mockCommandLine).hasOption(expectedOpt1);

        String expectedOpt2 = "b";
        ApplicationOption mockOption2 = getCreateMockApplicationOption(expectedOpt2, false, ArgumentType.NO_ARGS);
        doReturn(Boolean.TRUE).when(mockCommandLine).hasOption(expectedOpt2);

        applicationOptionList.add(mockOption1);
        applicationOptionList.add(mockOption2);

        expectedArgs = new String[]{"-" + expectedOpt1, "-" + expectedOpt2};

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption1).exec();
        verify(mockOption2, never()).exec();
    }

    @Test
    public void shouldInvokeDefaultOption() {
        doReturn(Boolean.FALSE).when(mockCommandLine).hasOption(expectedOpt);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption, never()).exec();
        verify(mockDefaultOption).exec();
    }

    @Test
    public void shouldInvokeDefaultOptionIfOptionNotIsFinal() {
        doReturn(Boolean.TRUE).when(mockCommandLine).hasOption(expectedOpt);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption).exec();
        verify(mockDefaultOption).exec();
    }

    private ApplicationOption getCreateMockApplicationOption(String option, boolean isFinalOption, ArgumentType argumentType) {
        ApplicationOption mock = mock(ApplicationOption.class);
        doCallRealMethod().when(mock).toOption();
        doCallRealMethod().when(mock).getKeyOpt();
        doReturn(option).when(mock).getLongOpt();
        doReturn(isFinalOption).when(mock).isFinalOption();
        doReturn(argumentType).when(mock).getArgType();
        return mock;
    }

}