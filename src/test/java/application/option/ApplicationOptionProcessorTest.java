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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

        when(mockApplicationOptions.getApplicationOptionList()).thenReturn(applicationOptionList);
        when(mockCommandLineParser.parse(any(), any())).thenReturn(mockCommandLine);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.TRUE);
        when(mockApplicationOptions.getDefaultApplicationOption()).thenReturn(mockDefaultOption);

        expectedArgs = new String[]{"-" + expectedOpt};
    }

    @Test
    public void shouldParseArgs() throws Exception {
        Options expectedOptions = new Options();

        when(mockApplicationOptions.toOptions()).thenReturn(expectedOptions);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockCommandLineParser).parse(eq(expectedOptions), eq(expectedArgs));
    }

    @Test
    public void shouldInvokeOptionAndSeValue() {
        String expectedOptionValue = "value";

        when(mockCommandLine.getOptionValue(any())).thenReturn(expectedOptionValue);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption).setValue(expectedOptionValue);
        verify(mockOption).exec();
        verify(mockLogger).startingOption(mockOption);
    }

    @Test
    public void shouldInvokeOptionAndSetProperties() {
        Properties mockProperties = mock(Properties.class);
        when(mockOption.getArgType()).thenReturn(ArgumentType.UNLIMITED_ARGS);
        when(mockCommandLine.getOptionProperties(any())).thenReturn(mockProperties);

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
        when(mockCommandLine.hasOption(expectedOpt2)).thenReturn(Boolean.TRUE);

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
        when(mockApplicationOptions.getApplicationOptionList()).thenReturn(applicationOptionList);

        String expectedOpt1 = "a";
        ApplicationOption mockOption1 = getCreateMockApplicationOption(expectedOpt1, true, ArgumentType.NO_ARGS);
        when(mockCommandLine.hasOption(expectedOpt1)).thenReturn(Boolean.TRUE);

        String expectedOpt2 = "b";
        ApplicationOption mockOption2 = getCreateMockApplicationOption(expectedOpt2, false, ArgumentType.NO_ARGS);
        when(mockCommandLine.hasOption(expectedOpt2)).thenReturn(Boolean.TRUE);

        applicationOptionList.add(mockOption1);
        applicationOptionList.add(mockOption2);

        expectedArgs = new String[]{"-" + expectedOpt1, "-" + expectedOpt2};

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption1).exec();
        verify(mockOption2, never()).exec();
    }

    @Test
    public void shouldInvokeDefaultOption() {
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.FALSE);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption, never()).exec();
        verify(mockDefaultOption).exec();
    }

    @Test
    public void shouldInvokeDefaultOptionIfOptionNotIsFinal() {
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.TRUE);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption).exec();
        verify(mockDefaultOption).exec();
    }

    private ApplicationOption getCreateMockApplicationOption(String option, boolean isFinalOption, ArgumentType argumentType) {
        ApplicationOption mock = mock(ApplicationOption.class);
        when(mock.toOption()).thenCallRealMethod();
        when(mock.getLongOpt()).thenReturn(option);
        when(mock.getKeyOpt()).thenCallRealMethod();
        when(mock.isFinalOption()).thenReturn(isFinalOption);
        when(mock.getArgType()).thenReturn(argumentType);
        return mock;
    }

}