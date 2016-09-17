/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JadeOptionTest {

    private JadeOption jadeOption;

    @Before
    public void setUp() {
        jadeOption = new JadeOption();
    }

    @Test
    public void shouldGetShortCommand() {
        assertThat(jadeOption.getOpt(), is("j"));
    }

    @Test
    public void shouldGetLongCommand() {
        assertThat(jadeOption.getLongOpt(), is("jade"));
    }

    @Test
    public void shouldGetDescriptionCommand() {
        assertThat(jadeOption.getDescription(), is("Start Jade framework with arguments"));
    }

    @Test
    public void shouldGetHasArgsCommand() {
        assertTrue(jadeOption.hasArg());
    }


}