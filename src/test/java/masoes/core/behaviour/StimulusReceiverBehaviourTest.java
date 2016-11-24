/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.core.EmotionalAgent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EmotionalAgent.class, Behaviour.class})
public class StimulusReceiverBehaviourTest {

    private EmotionalAgent mockEmotionalAgent;
    private StimulusReceiverBehaviour spyStimulusReceiverBehaviour;

    @Before
    public void setUp() {
        mockEmotionalAgent = mock(EmotionalAgent.class);
        spyStimulusReceiverBehaviour = spy(new StimulusReceiverBehaviour(mockEmotionalAgent));
    }

    @Test
    public void shouldBlockWhenMessageIsNull() {
        when(mockEmotionalAgent.receive(any())).thenReturn(null);
        spyStimulusReceiverBehaviour.action();
        verify(spyStimulusReceiverBehaviour).block();
    }

    @Test
    public void shouldEvaluateStimulus() {
        ACLMessage mockAclMessageRequest = mock(ACLMessage.class);
        when(mockEmotionalAgent.receive(any())).thenReturn(mockAclMessageRequest);
        spyStimulusReceiverBehaviour.action();
        verify(mockEmotionalAgent).evaluateStimulus(any());
    }

}