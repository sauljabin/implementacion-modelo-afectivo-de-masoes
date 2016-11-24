/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.core.Emotion;
import masoes.core.EmotionalAgent;
import masoes.core.EmotionalState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({EmotionalAgent.class, Behaviour.class})
public class ReplayAgentInformationBehaviourTest {

    private EmotionalAgent mockEmotionalAgent;
    private ReplayAgentInformationBehaviour spyReplayAgentInformationBehaviour;

    @Before
    public void setUp() {
        mockEmotionalAgent = mock(EmotionalAgent.class);
        spyReplayAgentInformationBehaviour = spy(new ReplayAgentInformationBehaviour(mockEmotionalAgent));
    }

    @Test
    public void shouldBlockWhenMessageIsNull() {
        when(mockEmotionalAgent.receive(any())).thenReturn(null);
        spyReplayAgentInformationBehaviour.action();
        verify(spyReplayAgentInformationBehaviour).block();
    }

    @Test
    public void shouldSendEmotionAndNameAndBehaviourState() {
        ACLMessage mockAclMessageRequest = mock(ACLMessage.class);
        ACLMessage mockAclMessageResponse = mock(ACLMessage.class);

        when(mockAclMessageRequest.createReply()).thenReturn(mockAclMessageResponse);
        when(mockEmotionalAgent.receive(any())).thenReturn(mockAclMessageRequest);

        String expectedName = "EXPECTED_NAME";
        when(mockEmotionalAgent.getName()).thenReturn(expectedName);

        Emotion mockEmotion = mock(Emotion.class);
        String expectedEmotion = "EXPECTED_EMOTION";
        when(mockEmotion.getName()).thenReturn(expectedEmotion);
        when(mockEmotionalAgent.getCurrentEmotion()).thenReturn(mockEmotion);


        Behaviour mockBehaviour = mock(Behaviour.class);
        String expectedBehaviour = "EXPECTED_BEHAVIOUR";
        when(mockBehaviour.getBehaviourName()).thenReturn(expectedBehaviour);
        when(mockEmotionalAgent.getEmotionalBehaviour()).thenReturn(mockBehaviour);

        EmotionalState emotionalState = new EmotionalState();
        String expectedEmotionalState = emotionalState.toString();
        when(mockEmotionalAgent.getEmotionalState()).thenReturn(emotionalState);

        Map<String, String> content = new HashMap<>();
        content.put("agent", expectedName);
        content.put("emotion", expectedEmotion);
        content.put("behaviour", expectedBehaviour);
        content.put("state", expectedEmotionalState);

        spyReplayAgentInformationBehaviour.action();
        verify(mockAclMessageResponse).setContent(content.toString());
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(mockEmotionalAgent).send(mockAclMessageResponse);
    }

}