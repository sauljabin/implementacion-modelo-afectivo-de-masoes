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
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
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
        mockOption = getCreateMockApplicationOption(expectedOpt);

        applicationOptionList = new ArrayList<>();
        applicationOptionList.add(mockOption);

        when(mockApplicationOptions.getApplicationOptionList()).thenReturn(applicationOptionList);
        when(mockCommandLineParser.parse(any(), any())).thenReturn(mockCommandLine);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.TRUE);

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
    public void shouldInvokeOption() {
        String expectedOptionValue = "value";
        Properties mockProperties = mock(Properties.class);

        when(mockCommandLine.getOptionValue(any())).thenReturn(expectedOptionValue);
        when(mockCommandLine.getOptionProperties(any())).thenReturn(mockProperties);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption).setValue(expectedOptionValue);
        verify(mockOption).setProperties(mockProperties);
        verify(mockOption).exec();
        verify(mockLogger).startingOption(mockOption);
    }

    @Test
    public void shouldInvokeTwoOptions() {
        String expectedOpt2 = "test";
        ApplicationOption mockOption2 = getCreateMockApplicationOption(expectedOpt2);
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
    public void shouldInvokeDefaultOption() {
        HelpOption mockHelOption = mock(HelpOption.class);

        when(mockApplicationOptions.getDefaultApplicationOption()).thenReturn(mockHelOption);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.FALSE);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockApplicationOptions).getDefaultApplicationOption();
        verify(mockHelOption).exec();
    }

    private ApplicationOption getCreateMockApplicationOption(String option) {
        ApplicationOption mock = mock(ApplicationOption.class);
        when(mock.toOption()).thenCallRealMethod();
        when(mock.getLongOpt()).thenReturn(option);
        when(mock.getKeyOpt()).thenCallRealMethod();
        return mock;
    }

}