/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationSettings;
import application.ArgumentType;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class HelpOptionTest {

    private HelpOption helpOption;
    private HelpFormatter helpFormatterMock;
    private ApplicationSettings applicationSettingsMock;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        helpFormatterMock = mock(HelpFormatter.class);
        helpOption = new HelpOption();
        setFieldValue(helpOption, "helpFormatter", helpFormatterMock);
        setFieldValue(helpOption, "applicationSettings", applicationSettingsMock);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(helpOption.getOpt(), is("h"));
        assertThat(helpOption.getLongOpt(), is("help"));
        assertThat(helpOption.getDescription(), is(notNullValue()));
        assertThat(helpOption.getArgType(), is(ArgumentType.NO_ARGS));
        assertThat(helpOption.getOrder(), is(20));
        assertTrue(helpOption.isFinalOption());
    }

    @Test
    public void shouldPrintHelp() {
        helpOption.exec();
        verify(helpFormatterMock).setSyntaxPrefix("Usage: ");
        verify(helpFormatterMock).setLongOptSeparator("=");
        verify(helpFormatterMock).printHelp(any(), any(Options.class));
    }

}