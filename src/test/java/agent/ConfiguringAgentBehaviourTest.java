package agent;

/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

import jade.core.Agent;
import ontology.configurable.ConfigurableOntology;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ConfiguringAgentBehaviourTest {

    private ConfiguringAgentBehaviour configuringAgentBehaviour;
    private Agent agentMock;

    @Before
    public void setUp() {
        agentMock = mock(Agent.class);
        configuringAgentBehaviour = new ConfiguringAgentBehaviour(agentMock);
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(configuringAgentBehaviour.getOntology(), is(instanceOf(ConfigurableOntology.class)));
        assertThat(configuringAgentBehaviour.getMessageTemplate(), is(instanceOf(ConfiguringAgentRequestMessageTemplate.class)));
    }

}