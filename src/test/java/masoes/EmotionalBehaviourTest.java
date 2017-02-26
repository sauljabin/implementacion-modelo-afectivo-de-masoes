/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.core.Agent;
import masoes.behavioural.BehaviourType;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.powermock.api.mockito.PowerMockito.mock;

public class EmotionalBehaviourTest extends PowerMockitoTest {

    private EmotionalBehaviour emotionalBehaviour;
    private Agent agentMock;

    @Before
    public void setUp() {
        emotionalBehaviour = createEmotionalBehaviour();
        agentMock = mock(Agent.class);
        emotionalBehaviour.setAgent(agentMock);
    }

    @Test
    public void shouldNotifyAction() {

    }

    private EmotionalBehaviour createEmotionalBehaviour() {
        return new EmotionalBehaviour() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public BehaviourType getType() {
                return null;
            }

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