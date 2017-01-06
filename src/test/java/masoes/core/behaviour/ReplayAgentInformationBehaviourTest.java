/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.logger.JadeLogger;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({EmotionalAgent.class, Behaviour.class})
public class ReplayAgentInformationBehaviourTest {

    private EmotionalAgent mockEmotionalAgent;
    private ReplayAgentInformationBehaviour spyReplayAgentInformationBehaviour;
    private JadeLogger mockLogger;
    private ACLMessage mockAclMessageRequest;
    private ACLMessage mockAclMessageResponse;
    private Map<String, Object> expectedContent;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        mockEmotionalAgent = mock(EmotionalAgent.class);
        mockLogger = mock(JadeLogger.class);

        ReplayAgentInformationBehaviour replayAgentInformationBehaviour = new ReplayAgentInformationBehaviour(mockEmotionalAgent);
        setFieldValue(replayAgentInformationBehaviour, "logger", mockLogger);

        spyReplayAgentInformationBehaviour = spy(replayAgentInformationBehaviour);

        mockAclMessageRequest = mock(ACLMessage.class);
        mockAclMessageResponse = mock(ACLMessage.class);

        when(mockEmotionalAgent.receive(any())).thenReturn(mockAclMessageRequest);
        when(mockAclMessageRequest.createReply()).thenReturn(mockAclMessageResponse);

        String expectedName = "EXPECTED_NAME";
        when(mockEmotionalAgent.getName()).thenReturn(expectedName);

        Emotion mockEmotion = mock(Emotion.class);
        String expectedEmotion = "EXPECTED_EMOTION";
        when(mockEmotion.getEmotionName()).thenReturn(expectedEmotion);
        when(mockEmotionalAgent.getCurrentEmotion()).thenReturn(mockEmotion);

        Behaviour mockBehaviour = mock(Behaviour.class);
        String expectedBehaviour = "EXPECTED_BEHAVIOUR";
        when(mockBehaviour.getBehaviourName()).thenReturn(expectedBehaviour);
        when(mockEmotionalAgent.getEmotionalBehaviour()).thenReturn(mockBehaviour);

        EmotionalState emotionalState = new EmotionalState();
        when(mockEmotionalAgent.getEmotionalState()).thenReturn(emotionalState);

        expectedContent = new HashMap<>();
        expectedContent.put("agent", expectedName);
        expectedContent.put("emotion", expectedEmotion);
        expectedContent.put("behaviour", expectedBehaviour);
        expectedContent.put("state", emotionalState);
    }

    @Test
    public void shouldNotSendInfoOfBehaviourIfIsNull() throws Exception {
        when(mockEmotionalAgent.getEmotionalBehaviour()).thenReturn(null);
        expectedContent.put("behaviour", "NO BEHAVIOUR");
        spyReplayAgentInformationBehaviour.action();
        verify(mockAclMessageResponse).setContent(objectMapper.writeValueAsString(expectedContent));
    }

    @Test
    public void shouldNotSendInfoOfEmotionIfIsNull() throws Exception {
        when(mockEmotionalAgent.getCurrentEmotion()).thenReturn(null);
        when(mockEmotionalAgent.getEmotionalState()).thenReturn(null);
        expectedContent.put("emotion", "NO EMOTION");
        expectedContent.put("state", "NO STATE");
        spyReplayAgentInformationBehaviour.action();
        verify(mockAclMessageResponse).setContent(objectMapper.writeValueAsString(expectedContent));
    }

    @Test
    public void shouldBlockWhenMessageIsNull() {
        when(mockEmotionalAgent.receive(any())).thenReturn(null);
        spyReplayAgentInformationBehaviour.action();
        verify(spyReplayAgentInformationBehaviour).block();
    }

    @Test
    public void shouldSendEmotionAndNameAndBehaviourState() throws Exception {
        spyReplayAgentInformationBehaviour.action();
        verify(mockAclMessageResponse).setContent(objectMapper.writeValueAsString(expectedContent));
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(mockEmotionalAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldSendFailure() throws Exception {
        String expectedMessage = "";
        RuntimeException toBeThrown = new RuntimeException(expectedMessage);
        doThrow(toBeThrown).when(mockAclMessageResponse).setContent(objectMapper.writeValueAsString(expectedContent));
        spyReplayAgentInformationBehaviour.action();
        verify(mockAclMessageResponse).setContent(expectedMessage);
        verify(mockAclMessageResponse).setPerformative(ACLMessage.FAILURE);
        verify(mockEmotionalAgent).send(mockAclMessageResponse);
        verify(mockLogger).agentException(mockEmotionalAgent, toBeThrown);

    }

    @Test
    public void shouldLogMessage() {
        spyReplayAgentInformationBehaviour.action();
        verify(mockLogger).agentMessage(mockEmotionalAgent, mockAclMessageRequest);
    }

}