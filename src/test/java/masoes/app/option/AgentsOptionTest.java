/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.jade.JadeBoot;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AgentsOptionTest {

    private AgentsOption agentsOption;
    private JadeBoot mockJadeBoot;

    @Before
    public void setUp() {
        mockJadeBoot = mock(JadeBoot.class);
        agentsOption = new AgentsOption(mockJadeBoot);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        String expectedDescription = "Starts JADE with agents, examples:\n" +
                "Adds agents: \n" +
                "  --agents=\"<name>:<class>;<name>:<class>\"\n" +
                "Agent arguments: \n" +
                "  --agents=\"<name>:<class>(arg1,arg2)\"";

        assertThat(agentsOption.getOpt(), is("a"));
        assertThat(agentsOption.getLongOpt(), is("agents"));
        assertThat(agentsOption.getDescription(), is(expectedDescription));
        assertTrue(agentsOption.hasArg());
        assertThat(agentsOption.getOrder(), is(40));
    }

    @Test
    public void shouldInvokeJadeBoot() {
        String expectedArgs = "a1:ClassName";
        agentsOption.exec(expectedArgs);
        verify(mockJadeBoot).boot(expectedArgs);
    }

}