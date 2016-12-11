/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.jade.JadeBoot;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class AgentsOptionTest {

    private AgentsOption agentsOption;
    private JadeBoot mockJadeBoot;

    @Before
    public void setUp() throws Exception {
        mockJadeBoot = mock(JadeBoot.class);
        agentsOption = new AgentsOption();
        setFieldValue(agentsOption, "jadeBoot", mockJadeBoot);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        String expectedDescription = "Starts JADE with agents";

        assertThat(agentsOption.getOpt(), is("a"));
        assertThat(agentsOption.getLongOpt(), is("agents"));
        assertThat(agentsOption.getDescription(), containsString(expectedDescription));
        assertThat(agentsOption.getArgType(), is(ArgumentType.ONE_ARG));
        assertThat(agentsOption.getOrder(), is(40));
    }

    @Test
    public void shouldInvokeJadeBoot() {
        String expectedArgs = "a1:ClassName";
        agentsOption.setValue(expectedArgs);
        agentsOption.exec();
        verify(mockJadeBoot).boot(expectedArgs);
    }

}