/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ApplicationOptionProcessorTest {

    private ApplicationOptionProcessor cli;
    private Options options;
    private CommandLineParser mockCommandLineParser;
    private CommandLine mockCommandLine;

    @Before
    public void setUp() {
        options = new Options();
        mockCommandLineParser = mock(DefaultParser.class);
        mockCommandLine = mock(CommandLine.class);
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
        String opt = "h";
        String[] args = {"-" + opt};

        ApplicationOption mockOption = mock(HelpOption.class);
        when(mockOption.getOpt()).thenReturn(opt);
        options.addOption(mockOption);

        when(mockCommandLineParser.parse(any(), eq(args))).thenReturn(mockCommandLine);
        when(mockCommandLine.hasOption(opt)).thenReturn(Boolean.TRUE);

        cli = new ApplicationOptionProcessor(options, mockCommandLineParser);
        cli.processArgs(args);
        verify(mockOption).exec();
    }

}