/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class OptionWrapperTest {

    private Option optionVersion;
    private Option optionJade;
    private Option optionA;
    private Option optionB;
    private Option optionHelp;
    private Option optionC;

    @Before
    public void setUp() {
        optionVersion = new VersionOption().toOption();
        optionJade = new AgentsOption().toOption();
        optionHelp = new HelpOption().toOption();
        optionA = new Option("a", null);
        optionB = new Option("b", null);
        optionC = new Option("c", null);
    }

    @Test
    public void shouldReturnVersionLessJade() {
        assertThat(OptionWrapper.comparator().compare(optionVersion, optionJade), is(lessThan(0)));
    }

    @Test
    public void shouldReturnALessB() {
        assertThat(OptionWrapper.comparator().compare(optionA, optionB), is(lessThan(0)));
    }

    @Test
    public void shouldReturnCLessHelp() {
        assertThat(OptionWrapper.comparator().compare(optionC, optionHelp), is(lessThan(0)));
    }

}