/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ApplicationOptionProcessorTest {

    private ApplicationOptionProcessor cli;
    private ApplicationOptions options;
    private CommandLineParser mockCommandLineParser;
    private CommandLine mockCommandLine;

    @Before
    public void setUp() throws ParseException {
        options = new ApplicationOptions();
        mockCommandLineParser = mock(DefaultParser.class);
        mockCommandLine = mock(CommandLine.class);
        when(mockCommandLineParser.parse(any(), any())).thenReturn(mockCommandLine);
    }

    @Test
    public void shouldParseArgs() throws Exception {
        String[] args = {};
        cli = new ApplicationOptionProcessor(options, mockCommandLineParser);
        cli.processArgs(args);
        verify(mockCommandLineParser).parse(any(), eq(args));
    }

    @Test
    public void shouldInvokeOption() throws Exception {
        String opt = "test";
        String[] args = {"-" + opt};

        when(mockCommandLine.hasOption(opt)).thenReturn(Boolean.TRUE);

        ApplicationOption mockOption = mock(ApplicationOption.class);
        when(mockOption.getOpt()).thenReturn(opt);
        options.addOption(mockOption);

        cli = new ApplicationOptionProcessor(options, mockCommandLineParser);
        cli.processArgs(args);
        verify(mockOption).exec(any());
    }

}