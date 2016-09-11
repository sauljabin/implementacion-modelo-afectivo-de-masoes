/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import masoes.app.CommandLineInterface;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandLineInterfaceTest {

    private CommandLineInterface cli;

    @Before
    public void setUp() {
        cli = new CommandLineInterface();
    }

    @Test
    public void shouldReturnTrueWhenHasShortOptionHelp() {
        String expectedOption = "-h";
        String[] args = new String[]{expectedOption};
        cli.processArgs(args);
        assertTrue(cli.hasOption(expectedOption));
    }

    @Test
    public void shouldReturnTrueWhenHasLongOptionHelp() {
        String expectedOption = "--help";
        String[] args = new String[]{expectedOption};
        cli.processArgs(args);
        assertTrue(cli.hasOption(expectedOption));
    }

    @Test
    public void shouldInvokeHelpWhenHasOptionHelp(){
        CommandLineInterface mockCli = Mockito.spy(CommandLineInterface.class);
        mockCli.processArgs(new String[]{"-h"});
        Mockito.verify(mockCli, Mockito.times(1)).execHelp();
    }

    @Test
    public void shouldNotInvokeHelpWhenNoHasOptionHelp(){
        CommandLineInterface mockCli = Mockito.spy(CommandLineInterface.class);
        mockCli.processArgs(new String[]{});
        Mockito.verify(mockCli, Mockito.times(0)).execHelp();
    }

    @Test
    public void shouldNotInvokeVersionWhenNoHasOptionVersion(){
        CommandLineInterface mockCli = Mockito.spy(CommandLineInterface.class);
        mockCli.processArgs(new String[]{});
        Mockito.verify(mockCli, Mockito.times(0)).execVersion();
    }

    @Test
    public void shouldInvokeVersionWhenHasOptionVersion(){
        CommandLineInterface mockCli = Mockito.spy(CommandLineInterface.class);
        mockCli.processArgs(new String[]{"-v"});
        Mockito.verify(mockCli, Mockito.times(1)).execVersion();
    }

}