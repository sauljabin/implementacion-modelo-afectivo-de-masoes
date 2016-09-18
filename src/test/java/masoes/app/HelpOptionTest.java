/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HelpOptionTest {

    private HelpOption helpOption;
    private HelpFormatter mockFormatter;
    private Options options;

    @Before
    public void setUp() {
        mockFormatter = mock(HelpFormatter.class);
        options = new Options();
        helpOption = new HelpOption(options, mockFormatter);
    }

    @Test
    public void shouldGetShortCommand() {
        assertThat(helpOption.getOpt(), is("h"));
    }

    @Test
    public void shouldGetLongCommand() {
        assertThat(helpOption.getLongOpt(), is("help"));
    }

    @Test
    public void shouldGetDescriptionCommand() {
        assertThat(helpOption.getDescription(), is("Shows the options"));
    }

    @Test
    public void shouldGetHasArgsCommand() {
        assertFalse(helpOption.hasArg());
    }

    @Test
    public void shouldGetSecondOrder() {
        assertThat(helpOption.getOrder(), is(2));
    }

    @Test
    public void shouldPrintHelp() {
        helpOption.exec("");
        verify(mockFormatter).printHelp(any(), eq(options));
    }
}