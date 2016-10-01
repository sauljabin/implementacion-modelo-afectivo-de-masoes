/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HelpOptionTest {

    private HelpOption helpOption;
    private HelpFormatter mockFormatter;

    @Before
    public void setUp() {
        mockFormatter = mock(HelpFormatter.class);
        helpOption = new HelpOption(mockFormatter);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(helpOption.getOpt(), is("h"));
        assertThat(helpOption.getLongOpt(), is("help"));
        assertThat(helpOption.getDescription(), is("Shows the options"));
        assertFalse(helpOption.hasArg());
        assertThat(helpOption.getOrder(), is(20));
    }

    @Test
    public void shouldConfigFormatter() {
        verify(mockFormatter).setSyntaxPrefix("Usage: ");
        verify(mockFormatter).setLongOptSeparator("=");
        verify(mockFormatter).setOptionComparator(OptionWrapper.comparator());
    }

    @Test
    public void shouldPrintHelp() {
        helpOption.exec("");
        verify(mockFormatter).printHelp(any(), any(Options.class));
    }

}