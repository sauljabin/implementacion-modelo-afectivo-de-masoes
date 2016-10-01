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
    private ApplicationOptionManager applicationOptionManager;
    private String expectedOpt;
    private String[] expectedArgs;
    private List<ApplicationOption> applicationOptions;

    private CommandLineParser mockCommandLineParser;
    private CommandLine mockCommandLine;
    private ApplicationLogger mockLogger;
    private ApplicationOption mockOption;
    private ApplicationOption mockOption2;
    private String expectedOpt2;

    @Before
    public void setUp() throws ParseException {
        mockCommandLineParser = mock(DefaultParser.class);
        mockCommandLine = mock(CommandLine.class);
        mockLogger = mock(ApplicationLogger.class);

        mockOption = mock(ApplicationOption.class);
        mockOption2 = mock(ApplicationOption.class);

        applicationOptions = new ArrayList<>();
        applicationOptions.add(mockOption);
        applicationOptions.add(mockOption2);
        applicationOptionManager = mock(ApplicationOptionManager.class);

        applicationOptionProcessor = new ApplicationOptionProcessor(applicationOptionManager, mockCommandLineParser, mockLogger);

        expectedOpt = "test";
        expectedOpt2 = "test";
        when(mockOption.getLongOpt()).thenReturn(expectedOpt);
        when(mockOption.toOption()).thenCallRealMethod();
        when(mockOption.getKeyOpt()).thenCallRealMethod();
        when(mockOption2.getLongOpt()).thenReturn(expectedOpt2);
        when(mockOption2.toOption()).thenCallRealMethod();
        when(mockOption2.getKeyOpt()).thenCallRealMethod();

        when(applicationOptionManager.getApplicationOptions()).thenReturn(applicationOptions);
        when(mockCommandLineParser.parse(any(), any())).thenReturn(mockCommandLine);
        when(mockCommandLine.getOptionValue(any())).thenReturn("");
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.TRUE);
        when(mockCommandLine.hasOption(expectedOpt2)).thenReturn(Boolean.TRUE);

        expectedArgs = new String[]{"-" + expectedOpt};
    }

    @Test
    public void shouldParseArgs() throws Exception {
        applicationOptionProcessor.processArgs(expectedArgs);
        verify(mockCommandLineParser).parse(any(Options.class), eq(expectedArgs));
    }

    @Test
    public void shouldInvokeOption() throws Exception {
        applicationOptionProcessor.processArgs(expectedArgs);
        verify(mockOption).exec(any());
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

        when(applicationOptionManager.getDefaultOption()).thenReturn(mockHelOption);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.FALSE);

        applicationOptionProcessor.processArgs(expectedArgs);

        verify(applicationOptionManager).getDefaultOption();
        verify(mockHelOption).exec(any());
    }

}