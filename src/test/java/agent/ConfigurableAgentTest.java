/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ConfigurableAgentTest {

    private ConfigurableAgent configurableAgent;
    private ConfigurableAgent configurableAgentSpy;

    @Before
    public void setUp() {
        configurableAgent = new ConfigurableAgent();
        configurableAgentSpy = spy(configurableAgent);
    }

    @Test
    public void shouldAddConfiguringAgentBehaviour() {
        configurableAgentSpy.setup();
        verify(configurableAgentSpy).addBehaviour(isA(ConfiguringAgentBehaviour.class));
    }

}