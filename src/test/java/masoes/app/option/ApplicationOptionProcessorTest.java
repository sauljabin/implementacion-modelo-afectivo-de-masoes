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

    private ApplicationOptionProcessor optionProcessor;
    private ApplicationOptionManager mockOptions;
    private CommandLineParser mockCommandLineParser;
    private CommandLine mockCommandLine;
    private String expectedOpt;
    private String[] expectedArgs;
    private ApplicationOption mockOption;
    private List<ApplicationOption> applicationOptions;
    private ApplicationLogger mockLogger;

    @Before
    public void setUp() throws ParseException {
        mockCommandLineParser = mock(DefaultParser.class);
        mockCommandLine = mock(CommandLine.class);
        mockOption = mock(ApplicationOption.class);
        mockLogger = mock(ApplicationLogger.class);

        applicationOptions = new ArrayList<>();
        applicationOptions.add(mockOption);
        mockOptions = mock(ApplicationOptionManager.class);

        optionProcessor = new ApplicationOptionProcessor(mockOptions, mockCommandLineParser, mockLogger);

        expectedOpt = "test";
        expectedArgs = new String[]{"-" + expectedOpt};

        when(mockOptions.getApplicationOptions()).thenReturn(applicationOptions);
        when(mockOption.getOpt()).thenReturn(expectedOpt);
        when(mockOption.getLongOpt()).thenReturn(expectedOpt);
        when(mockOption.toOption()).thenCallRealMethod();
        when(mockOption.getKeyOpt()).thenCallRealMethod();
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.TRUE);
        when(mockCommandLineParser.parse(any(), any())).thenReturn(mockCommandLine);
        when(mockCommandLine.getOptionValue(any())).thenReturn("");
    }

    @Test
    public void shouldParseArgs() throws Exception {
        optionProcessor.processArgs(expectedArgs);
        verify(mockCommandLineParser).parse(any(Options.class), eq(expectedArgs));
    }

    @Test
    public void shouldInvokeOption() throws Exception {
        optionProcessor.processArgs(expectedArgs);
        verify(mockOption).exec(any());
        verify(mockLogger).startingOption(mockOption);
    }

    @Test
    public void shouldInvokeDefaultOption() {
        HelpOption mockHelOption = mock(HelpOption.class);

        when(mockOptions.getDefaultOption()).thenReturn(mockHelOption);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.FALSE);

        optionProcessor.processArgs(expectedArgs);

        verify(mockOptions).getDefaultOption();
        verify(mockHelOption).exec(any());
    }

}