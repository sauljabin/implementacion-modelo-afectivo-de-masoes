/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.logger;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.core.Emotion;
import masoes.core.EmotionalAgent;
import masoes.core.EmotionalState;
import masoes.core.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Agent.class, Behaviour.class})
public class JadeLoggerTest {

    private Logger mockLogger;
    private JadeLogger jadeLogger;

    @Before
    public void setUp() {
        mockLogger = mock(Logger.class);
        jadeLogger = new JadeLogger(mockLogger);
    }

    @Test
    public void shouldLogAgentException() {
        Agent mockAgent = mock(Agent.class);
        String expectedAgentName = "agent";
        when(mockAgent.getLocalName()).thenReturn(expectedAgentName);

        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("Exception in agent \"%s\": %s", expectedAgentName, expectedException.getMessage());

        jadeLogger.agentException(mockAgent, expectedException);
        verify(mockLogger).error(eq(expectedMessage), eq(expectedException));
    }

    @Test
    public void shouldLogAgentMessage() {
        String expectedAgentName = "agent";
        Agent mockAgent = mock(Agent.class);
        when(mockAgent.getLocalName()).thenReturn(expectedAgentName);

        ACLMessage mockMessage = mock(ACLMessage.class);
        when(mockMessage.toString()).thenReturn("message");

        String expectedMessage = String.format("Message in agent \"%s\": %s", expectedAgentName, mockMessage);

        jadeLogger.agentMessage(mockAgent, mockMessage);
        verify(mockLogger).info(eq(expectedMessage));
    }

    @Test
    public void shouldLogAgentEmotionalState() {
        String expectedAgentName = "agent";
        EmotionalAgent mockAgent = mock(EmotionalAgent.class);
        when(mockAgent.getLocalName()).thenReturn(expectedAgentName);

        Emotion mockEmotion = mock(Emotion.class);
        when(mockEmotion.toString()).thenReturn("emotion");
        when(mockAgent.getCurrentEmotion()).thenReturn(mockEmotion);

        EmotionalState emotionalState = new EmotionalState();
        when(mockAgent.getEmotionalState()).thenReturn(emotionalState);

        Behaviour mockBehaviour = mock(Behaviour.class);
        String expectedBehaviourName = "behaviour";
        when(mockBehaviour.getBehaviourName()).thenReturn(expectedBehaviourName);
        when(mockAgent.getEmotionalBehaviour()).thenReturn(mockBehaviour);

        String expectedMessage = String.format("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s", expectedAgentName, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalState(mockAgent);
        verify(mockLogger).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        when(mockAgent.getEmotionalBehaviour()).thenReturn(null);
        expectedMessage = String.format("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s", expectedAgentName, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalState(mockAgent);
        verify(mockLogger).info(eq(expectedMessage));
    }

    @Test
    public void shouldLogAgentEmotionalStateChanged() {
        String expectedAgentName = "agent";
        EmotionalAgent mockAgent = mock(EmotionalAgent.class);
        when(mockAgent.getLocalName()).thenReturn(expectedAgentName);

        Stimulus mockStimulus = mock(Stimulus.class);
        when(mockStimulus.toString()).thenReturn("stimulus");

        Emotion mockEmotion = mock(Emotion.class);
        when(mockEmotion.toString()).thenReturn("emotion");
        when(mockAgent.getCurrentEmotion()).thenReturn(mockEmotion);

        EmotionalState emotionalState = new EmotionalState();
        when(mockAgent.getEmotionalState()).thenReturn(emotionalState);

        Behaviour mockBehaviour = mock(Behaviour.class);
        String expectedBehaviourName = "behaviour";
        when(mockBehaviour.getBehaviourName()).thenReturn(expectedBehaviourName);
        when(mockAgent.getEmotionalBehaviour()).thenReturn(mockBehaviour);

        String expectedMessage = String.format("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s", expectedAgentName, mockStimulus, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalStateChanged(mockAgent, mockStimulus);
        verify(mockLogger).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        when(mockAgent.getEmotionalBehaviour()).thenReturn(null);
        expectedMessage = String.format("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s", expectedAgentName, mockStimulus, mockEmotion, emotionalState, expectedBehaviourName);

        jadeLogger.agentEmotionalStateChanged(mockAgent, mockStimulus);
        verify(mockLogger).info(eq(expectedMessage));
    }

}