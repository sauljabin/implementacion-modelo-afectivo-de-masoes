/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptionWrapperTest {

    private Option optionA;
    private Option optionB;
    private Option optionC;
    private OptionWrapper optionD;
    private OptionWrapper optionE;
    private OptionWrapper optionF;
    private ApplicationOption mockApplicationOptionD;
    private ApplicationOption mockApplicationOptionE;
    private ApplicationOption mockApplicationOptionF;
    private Comparator<Option> comparator;

    @Before
    public void setUp() {
        comparator = OptionWrapper.comparator();
        optionA = new Option("a", null);
        optionB = new Option("b", null);
        optionC = new Option("c", null);

        mockApplicationOptionD = mock(ApplicationOption.class);
        when(mockApplicationOptionD.getOrder()).thenReturn(10);
        optionD = new OptionWrapper(mockApplicationOptionD);

        mockApplicationOptionE = mock(ApplicationOption.class);
        when(mockApplicationOptionE.getOrder()).thenReturn(20);
        optionE = new OptionWrapper(mockApplicationOptionE);

        mockApplicationOptionF = mock(ApplicationOption.class);
        when(mockApplicationOptionF.getOpt()).thenReturn("f");
        optionF = new OptionWrapper(mockApplicationOptionF);
    }

    @Test
    public void shouldReturnALessThanB() {
        assertThat(comparator.compare(optionA, optionB), is(lessThan(0)));
    }

    @Test
    public void shouldReturnDLessThanE() {
        assertThat(comparator.compare(optionD, optionE), is(lessThan(0)));
    }

    @Test
    public void shouldReturnCLessThanF() {
        assertThat(comparator.compare(optionC, optionF), is(lessThan(0)));
    }

    @Test
    public void shouldReturnCorrectOrder() {
        assertThat(optionD.getOrder(), is(mockApplicationOptionD.getOrder()));
        assertThat(optionE.getOrder(), is(mockApplicationOptionE.getOrder()));
    }

}