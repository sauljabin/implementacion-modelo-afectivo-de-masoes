/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.app.logger.ApplicationLogger;
import masoes.core.EmotionalAgent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({EmotionalAgent.class, Behaviour.class})
public class StimulusReceiverBehaviourTest {

    private EmotionalAgent mockEmotionalAgent;
    private StimulusReceiverBehaviour spyStimulusReceiverBehaviour;
    private ApplicationLogger mockApplicationLogger;
    private ACLMessage mockAclMessageRequest;

    @Before
    public void setUp() {
        mockEmotionalAgent = mock(EmotionalAgent.class);
        mockApplicationLogger = mock(ApplicationLogger.class);
        spyStimulusReceiverBehaviour = spy(new StimulusReceiverBehaviour(mockEmotionalAgent, mockApplicationLogger));
        mockAclMessageRequest = mock(ACLMessage.class);

        when(mockEmotionalAgent.receive(any())).thenReturn(mockAclMessageRequest);
    }

    @Test
    public void shouldBlockWhenMessageIsNull() {
        when(mockEmotionalAgent.receive(any())).thenReturn(null);
        spyStimulusReceiverBehaviour.action();
        verify(spyStimulusReceiverBehaviour).block();
    }

    @Test
    public void shouldEvaluateStimulus() {
        spyStimulusReceiverBehaviour.action();
        verify(mockEmotionalAgent).evaluateStimulus(any());
    }

    @Test
    public void shouldLogMessage() {
        spyStimulusReceiverBehaviour.action();
        verify(mockApplicationLogger).agentMessage(mockEmotionalAgent, mockAclMessageRequest);
    }

}