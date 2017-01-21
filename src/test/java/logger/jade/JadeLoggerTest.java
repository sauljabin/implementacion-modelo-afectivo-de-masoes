/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package logger.jade;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.model.Emotion;
import masoes.model.EmotionalAgent;
import masoes.model.EmotionalState;
import masoes.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Agent.class, Behaviour.class})
public class JadeLoggerTest {

    private static final String EXPECTED_AGENT_NAME = "AGENT";
    private Logger mockLogger;
    private JadeLogger jadeLogger;
    private EmotionalAgent mockAgent;
    private Behaviour mockBehaviour;

    @Before
    public void setUp() {
        mockLogger = mock(Logger.class);
        jadeLogger = new JadeLogger(mockLogger);
        mockAgent = mock(EmotionalAgent.class);
        doReturn(EXPECTED_AGENT_NAME).when(mockAgent).getLocalName();
        mockBehaviour = mock(Behaviour.class);
    }

    @Test
    public void shouldLogAgentException() {
        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("Exception in agent \"%s\": %s", EXPECTED_AGENT_NAME, expectedException.getMessage());
        jadeLogger.agentException(mockAgent, expectedException);
        verify(mockLogger).error(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldLogAgentMessage() {
        ACLMessage mockMessage = mock(ACLMessage.class);
        doReturn("message").when(mockMessage).toString();
        String expectedMessage = String.format("Message in agent \"%s\": %s", EXPECTED_AGENT_NAME, mockMessage);
        jadeLogger.agentMessage(mockAgent, mockMessage);
        verify(mockLogger).info(eq(expectedMessage));
    }

    @Test
    public void shouldLogAgentEmotionalState() {
        Emotion mockEmotion = mock(Emotion.class);
        doReturn("emotion").when(mockEmotion).toString();
        doReturn(mockEmotion).when(mockAgent).getCurrentEmotion();
        EmotionalState emotionalState = new EmotionalState();
        doReturn(emotionalState).when(mockAgent).getEmotionalState();

        String expectedBehaviourName = "behaviour";
        doReturn(expectedBehaviourName).when(mockBehaviour).getBehaviourName();
        doReturn(mockBehaviour).when(mockAgent).getCurrentEmotionalBehaviour();

        String expectedMessage = String.format("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalState(mockAgent);
        verify(mockLogger).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        doReturn(null).when(mockAgent).getCurrentEmotionalBehaviour();
        expectedMessage = String.format("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalState(mockAgent);
        verify(mockLogger).info(eq(expectedMessage));
    }

    @Test
    public void shouldLogAgentEmotionalStateChanged() {
        Stimulus mockStimulus = mock(Stimulus.class);
        doReturn("stimulus").when(mockStimulus).toString();

        Emotion mockEmotion = mock(Emotion.class);
        doReturn("emotion").when(mockEmotion).toString();
        doReturn(mockEmotion).when(mockAgent).getCurrentEmotion();

        EmotionalState emotionalState = new EmotionalState();
        doReturn(emotionalState).when(mockAgent).getEmotionalState();

        String expectedBehaviourName = "behaviour";
        doReturn(expectedBehaviourName).when(mockBehaviour).getBehaviourName();
        doReturn(mockBehaviour).when(mockAgent).getCurrentEmotionalBehaviour();

        String expectedMessage = String.format("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, mockStimulus, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalStateChanged(mockAgent, mockStimulus);
        verify(mockLogger).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        doReturn(null).when(mockAgent).getCurrentEmotionalBehaviour();
        expectedMessage = String.format("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s", EXPECTED_AGENT_NAME, mockStimulus, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalStateChanged(mockAgent, mockStimulus);
        verify(mockLogger).info(eq(expectedMessage));
    }

}