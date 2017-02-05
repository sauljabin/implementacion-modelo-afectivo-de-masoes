/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.agent;

import functional.test.FunctionalTest;
import jade.core.AID;
import jade.wrapper.ControllerException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class AgentProtocolAssistantFunctionalTest extends FunctionalTest {

    @Test
    public void shouldCreateAndKillAgent() throws ControllerException {
        AID agent = createAgent();
        getAgent(agent.getLocalName());
        killAgent(agent);
        try {
            getAgent(agent.getLocalName());
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(agent.getName()));
        }
    }

}
