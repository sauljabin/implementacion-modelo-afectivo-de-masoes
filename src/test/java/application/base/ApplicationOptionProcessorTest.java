/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.base;

import application.exception.ApplicationOptionProcessorException;
import logger.writer.ApplicationLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ApplicationOptionProcessorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ApplicationOptionProcessor applicationOptionProcessor;
    private ApplicationOptions applicationOptionsMock;
    private String expectedOpt;
    private String[] expectedArgs;
    private List<ApplicationOption> applicationOptionList;

    private CommandLineParser commandLineParserMock;
    private CommandLine commandLineMock;
    private ApplicationLogger loggerMock;
    private ApplicationOption optionMock;
    private ApplicationOption defaultOptionMock;
    private String defaultKeyOption;

    @Before
    public void setUp() throws Exception {
        commandLineParserMock = mock(DefaultParser.class);
        commandLineMock = mock(CommandLine.class);
        loggerMock = mock(ApplicationLogger.class);
        applicationOptionsMock = mock(ApplicationOptions.class);

        applicationOptionProcessor = new ApplicationOptionProcessor();
        setFieldValue(applicationOptionProcessor, "applicationOptions", applicationOptionsMock);
        setFieldValue(applicationOptionProcessor, "commandLineParser", commandLineParserMock);
        setFieldValue(applicationOptionProcessor, "logger", loggerMock);

        expectedOpt = "test";
        optionMock = getCreateMockApplicationOption(expectedOpt, false, ArgumentType.ONE_ARG);

        defaultKeyOption = "default";
        defaultOptionMock = getCreateMockApplicationOption(defaultKeyOption, false, ArgumentType.NO_ARGS);

        applicationOptionList = new ArrayList<>();
        applicationOptionList.add(optionMock);

        doReturn(applicationOptionList).when(applicationOptionsMock).getApplicationOptionList();
        doReturn(commandLineMock).when(commandLineParserMock).parse(any(), any());
        doReturn(Boolean.TRUE).when(commandLineMock).hasOption(expectedOpt);
        doReturn(defaultOptionMock).when(applicationOptionsMock).getDefaultApplicationOption();

        expectedArgs = new String[]{"-" + expectedOpt};
    }

    @Test
    public void shouldInvokeParseArgs() throws Exception {
        Options expectedOptions = new Options();
        doReturn(expectedOptions).when(applicationOptionsMock).toOptions();

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(commandLineParserMock).parse(eq(expectedOptions), eq(expectedArgs));
    }

    @Test
    public void shouldInvokeOptionAndSetValue() {
        String expectedOptionValue = "value";
        doReturn(expectedOptionValue).when(commandLineMock).getOptionValue(any());

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(optionMock).setValue(expectedOptionValue);
        verify(optionMock).exec();
        verify(loggerMock).startingOption(optionMock);
    }

    @Test
    public void shouldInvokeOptionAndSetProperties() {
        Properties propertiesMock = mock(Properties.class);
        doReturn(ArgumentType.UNLIMITED_ARGS).when(optionMock).getArgType();
        doReturn(propertiesMock).when(commandLineMock).getOptionProperties(any());

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(optionMock).setProperties(propertiesMock);
        verify(optionMock).exec();
        verify(loggerMock).startingOption(optionMock);
    }

    @Test
    public void shouldInvokeTwoOptions() {
        String expectedOpt2 = "test";
        ApplicationOption optionMock2 = getCreateMockApplicationOption(expectedOpt2, false, ArgumentType.NO_ARGS);
        applicationOptionList.add(optionMock2);
        doReturn(Boolean.TRUE).when(commandLineMock).hasOption(expectedOpt2);

        expectedArgs = new String[]{"-" + expectedOpt, "-" + expectedOpt2};

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(loggerMock).startingOption(optionMock);
        verify(loggerMock).startingOption(optionMock2);
        verify(optionMock).exec();
        verify(optionMock2).exec();
    }

    @Test
    public void shouldBreakWhenOptionIsAFinalOption() {
        applicationOptionList = new ArrayList<>();
        doReturn(applicationOptionList).when(applicationOptionsMock).getApplicationOptionList();

        String expectedOpt1 = "a";
        ApplicationOption optionMock1 = getCreateMockApplicationOption(expectedOpt1, true, ArgumentType.NO_ARGS);
        doReturn(Boolean.TRUE).when(commandLineMock).hasOption(expectedOpt1);

        String expectedOpt2 = "b";
        ApplicationOption optionMock2 = getCreateMockApplicationOption(expectedOpt2, false, ArgumentType.NO_ARGS);
        doReturn(Boolean.TRUE).when(commandLineMock).hasOption(expectedOpt2);

        applicationOptionList.add(optionMock1);
        applicationOptionList.add(optionMock2);

        expectedArgs = new String[]{"-" + expectedOpt1, "-" + expectedOpt2};

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(optionMock1).exec();
        verify(optionMock2, never()).exec();
    }

    @Test
    public void shouldInvokeDefaultOption() {
        doReturn(Boolean.FALSE).when(commandLineMock).hasOption(expectedOpt);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(optionMock, never()).exec();
        verify(defaultOptionMock).exec();
    }

    @Test
    public void shouldInvokeDefaultOptionIfOptionIsNotAFinalOption() {
        doReturn(Boolean.TRUE).when(commandLineMock).hasOption(expectedOpt);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(optionMock).exec();
        verify(defaultOptionMock).exec();
    }

    @Test
    public void shouldThrowApplicationOptionExceptionIfParseThrowsAException() throws Exception {
        String message = "MESSAGE";
        doThrow(new RuntimeException(message)).when(commandLineParserMock).parse(any(), any());
        expectedException.expectMessage(message);
        expectedException.expect(ApplicationOptionProcessorException.class);
        applicationOptionProcessor.processArgs(expectedArgs);
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