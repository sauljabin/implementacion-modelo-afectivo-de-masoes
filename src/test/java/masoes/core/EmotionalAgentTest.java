/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EmotionalAgentTest {

    private EmotionalAgent emotionalAgent;
    private Behaviour currentBehaviour;

    @Before
    public void setUp() {
        emotionalAgent = mock(EmotionalAgent.class);
        currentBehaviour = createBehaviour();
        when(emotionalAgent.getCurrentBehaviour()).thenReturn(currentBehaviour);
        doCallRealMethod().when(emotionalAgent).changeCurrentBehaviour(any());
    }

    @Test
    public void shouldChangeAgentBehaviour() {
        Behaviour expectedBehaviour = createBehaviour();
        emotionalAgent.changeCurrentBehaviour(expectedBehaviour);
        verify(emotionalAgent).removeBehaviour(currentBehaviour);
        verify(emotionalAgent).addBehaviour(expectedBehaviour);
        when(emotionalAgent.getCurrentBehaviour()).thenCallRealMethod();
        assertThat(emotionalAgent.getCurrentBehaviour(), is(expectedBehaviour));
    }

    public Behaviour createBehaviour() {
        return new Behaviour() {

            @Override
            public void action() {

            }

            @Override
            public boolean done() {
                return false;
            }
        };
    }

}