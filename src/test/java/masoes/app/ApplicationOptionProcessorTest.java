/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.logger.ApplicationLogger;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ApplicationOptionProcessorTest {

    private ApplicationOptionProcessor optionProcessor;
    private ApplicationOptions options;
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
        options = new ApplicationOptions(applicationOptions);
        optionProcessor = new ApplicationOptionProcessor(options, mockCommandLineParser, mockLogger);

        expectedOpt = "test";
        expectedArgs = new String[]{"-" + expectedOpt};

        when(mockOption.getOpt()).thenReturn(expectedOpt);
        when(mockCommandLine.hasOption(expectedOpt)).thenReturn(Boolean.TRUE);
        when(mockCommandLineParser.parse(any(), any())).thenReturn(mockCommandLine);
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

}