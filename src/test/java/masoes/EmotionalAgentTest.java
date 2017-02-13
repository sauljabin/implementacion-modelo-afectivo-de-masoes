/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.core.Agent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class EmotionalAgentTest {

    private static final String LOCAL_NAME = "localName";
    private EmotionalAgent spyEmotionalAgent;

    @Before
    public void setUp() throws Exception {
        spyEmotionalAgent = spy(createAgent());
        doReturn(LOCAL_NAME).when(spyEmotionalAgent).getLocalName();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        spyEmotionalAgent.setup();
        InOrder inOrder = inOrder(spyEmotionalAgent);
        inOrder.verify(spyEmotionalAgent).setUp();
        inOrder.verify(spyEmotionalAgent).addBehaviour(isA(BasicEmotionalAgentBehaviour.class));
    }

    @Test
    public void shouldCreateComponent() {
        spyEmotionalAgent.setup();
        assertThat(spyEmotionalAgent.getBehaviouralComponent(), is(notNullValue()));
    }

    private EmotionalAgent createAgent() {
        return new EmotionalAgent() {
            @Override
            public void setUp() {

            }

            @Override
            public String getKnowledgePath() {
                return null;
            }

            @Override
            public ImitativeBehaviour getImitativeBehaviour() {
                return null;
            }

            @Override
            public ReactiveBehaviour getReactiveBehaviour() {
                return null;
            }

            @Override
            public CognitiveBehaviour getCognitiveBehaviour() {
                return null;
            }
        };
    }

}