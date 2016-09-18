/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(jade.Boot.class)
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

    @Test
    public void shouldGetThirdOrder() {
        assertThat(jadeOption.getOrder(), is(3));
    }

    @Test
    public void shouldStartJade() throws Exception {
        PowerMockito.mockStatic(jade.Boot.class);
        jadeOption.exec("");
        PowerMockito.verifyStatic();
        jade.Boot.main("".split(" "));
    }

}