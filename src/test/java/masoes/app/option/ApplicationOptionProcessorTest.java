/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.logger.ApplicationLogger;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ApplicationOptionProcessorTest {

    private ApplicationOptionProcessor applicationOptionProcessor;
    private ApplicationOptions applicationOptions;
    private String expectedOpt;
    private String[] expectedArgs;
    private List<ApplicationOption> applicationOptionList;

    private CommandLineParser mockCommandLineParser;
    private CommandLine mockCommandLine;
    private ApplicationLogger mockLogger;
    private ApplicationOption mockOption;
    private ApplicationOption mockOption2;
    private String expectedOpt2;
    private String expectedOptionValue;

    @Before
    public void setUp() throws ParseException {
        mockCommandLineParser = mock(DefaultParser.class);
        mockCommandLine = mock(CommandLine.class);
        mockLogger = mock(ApplicationLogger.class);

        mockOption = mock(ApplicationOption.class);
        mockOption2 = mock(ApplicationOption.class);

        applicationOptionList = new ArrayList<>();
        applicationOptionList.add(mockOption);
        applicationOptionList.add(mockOption2);

        applicationOptions = mock(ApplicationOptions.class);

        applicationOptionProcessor = new ApplicationOptionProcessor(applicationOptions, mockCommandLineParser, mockLogger);

        expectedOpt = "test";
        expectedOpt2 = "test";
        when(mockOption.getLongOpt()).thenReturn(expectedOpt);
        when(mockOption.toOption()).thenCallRealMethod();
        when(mockOption.getKeyOpt()).thenCallRealMethod();
        when(mockOption2.getLongOpt()).thenReturn(expectedOpt2);
        when(mockOption2.toOption()).thenCallRealMethod();
        when(mockOption2.getKeyOpt()).thenCallRealMethod();

        expectedOptionValue = "value";
        when(applicationOptions.getApplicationOptionList()).thenReturn(applicationOptionList);
        when(mockCommandLineParser.parse(any(), any())).thenReturn(mockCommandLine);
        when(mockCommandLine.getOptionValue(any())).thenReturn(expectedOptionValue);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.TRUE);
        when(mockCommandLine.hasOption(expectedOpt2)).thenReturn(Boolean.TRUE);

        expectedArgs = new String[]{"-" + expectedOpt};
    }

    @Test
    public void shouldParseArgs() throws Exception {
        Options expectedOptions = new Options();

        when(applicationOptions.toOptions()).thenReturn(expectedOptions);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockCommandLineParser).parse(eq(expectedOptions), eq(expectedArgs));
    }

    @Test
    public void shouldInvokeOption() throws Exception {
        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockOption).exec(expectedOptionValue);
        verify(mockLogger).startingOption(mockOption);
    }

    @Test
    public void shouldInvokeTwoOptions() throws Exception {
        expectedArgs = new String[]{"-" + expectedOpt, "-" + expectedOpt2};

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(mockLogger).startingOption(mockOption);
        verify(mockLogger).startingOption(mockOption2);
        verify(mockOption).exec(any());
        verify(mockOption2).exec(any());
    }

    @Test
    public void shouldInvokeDefaultOption() {
        HelpOption mockHelOption = mock(HelpOption.class);

        when(applicationOptions.getDefaultApplicationOption()).thenReturn(mockHelOption);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.FALSE);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(applicationOptions).getDefaultApplicationOption();
        verify(mockHelOption).exec(any());
    }

}