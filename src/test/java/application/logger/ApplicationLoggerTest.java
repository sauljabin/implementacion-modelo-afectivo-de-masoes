/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.logger;

import application.option.ApplicationOption;
import application.settings.ApplicationSettings;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.settings.JadeSettings;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Agent.class, Behaviour.class})
public class ApplicationLoggerTest {

    private Logger mockLogger;
    private ApplicationLogger applicationLogger;
    private ApplicationSettings mockApplicationSettings;
    private JadeSettings mockJadeSettings;
    private Map<String, String> expectedApplicationSettingsMap;
    private Map<String, String> expectedJadeSettingsMap;

    @Before
    public void setUp() throws Exception {
        mockApplicationSettings = mock(ApplicationSettings.class);
        mockJadeSettings = mock(JadeSettings.class);
        mockLogger = mock(Logger.class);
        applicationLogger = new ApplicationLogger(mockLogger);

        expectedApplicationSettingsMap = new HashMap<>();
        expectedApplicationSettingsMap.put("key", "value");
        when(mockApplicationSettings.toMap()).thenReturn(expectedApplicationSettingsMap);
        when(mockApplicationSettings.toString()).thenCallRealMethod();

        expectedJadeSettingsMap = new HashMap<>();
        expectedJadeSettingsMap.put("key", "value");
        when(mockJadeSettings.toMap()).thenReturn(expectedJadeSettingsMap);
        when(mockJadeSettings.toString()).thenCallRealMethod();

        setFieldValue(applicationLogger, "jadeSettings", mockJadeSettings);
        setFieldValue(applicationLogger, "applicationSettings", mockApplicationSettings);
    }

    @Test
    public void shouldLogStartingApp() {
        String[] args = {"-h"};
        applicationLogger.startingApplication(args);
        verify(mockLogger).info(eq("Starting application with arguments: [-h], settings: " + expectedApplicationSettingsMap.toString() + ", jade settings: " + expectedJadeSettingsMap.toString()));
    }

    @Test
    public void shouldLogClosingApp() {
        applicationLogger.closingApplication();
        verify(mockLogger).info(eq("Closing application"));
    }

    @Test
    public void shouldLogCantNotStartApp() {
        Exception expectedException = new Exception();
        applicationLogger.cantNotStartApplication(expectedException);
        verify(mockLogger).error(contains("Could not start the application"), eq(expectedException));
    }

    @Test
    public void shouldLogStartingOption() {
        ApplicationOption applicationOption = mock(ApplicationOption.class);
        String expectedToString = "expectedToString";
        when(applicationOption.toString()).thenReturn(expectedToString);

        applicationLogger.startingOption(applicationOption);
        verify(mockLogger).info(eq("Starting option: " + expectedToString));
    }

    @Test
    public void shouldLogUpdatedSettings() {
        applicationLogger.updatedSettings();
        verify(mockLogger).info(eq("Updated settings: " + expectedApplicationSettingsMap.toString() + ", jade settings: " + expectedJadeSettingsMap.toString()));
    }

    @Test
    public void shouldLogException() {
        Exception expectedException = new Exception("error");
        applicationLogger.exception(expectedException);
        verify(mockLogger).error(eq("Exception: " + expectedException.getMessage()), eq(expectedException));
    }

    @Test
    public void shouldLogAgentException() {
        Agent mockAgent = mock(Agent.class);
        String expectedAgentName = "agent";
        when(mockAgent.getLocalName()).thenReturn(expectedAgentName);

        Exception expectedException = new Exception("error");
        String expectedMessage = String.format("Exception in agent \"%s\": %s", expectedAgentName, expectedException.getMessage());

        applicationLogger.agentException(mockAgent, expectedException);
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

        applicationLogger.agentMessage(mockAgent, mockMessage);
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

        applicationLogger.agentEmotionalState(mockAgent);
        verify(mockLogger).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        when(mockAgent.getEmotionalBehaviour()).thenReturn(null);
        expectedMessage = String.format("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s", expectedAgentName, mockEmotion, emotionalState, expectedBehaviourName);

        applicationLogger.agentEmotionalState(mockAgent);
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

        applicationLogger.agentEmotionalStateChanged(mockAgent, mockStimulus);
        verify(mockLogger).info(eq(expectedMessage));

        expectedBehaviourName = "NO BEHAVIOUR";
        when(mockAgent.getEmotionalBehaviour()).thenReturn(null);
        expectedMessage = String.format("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s", expectedAgentName, mockStimulus, mockEmotion, emotionalState, expectedBehaviourName);

        applicationLogger.agentEmotionalStateChanged(mockAgent, mockStimulus);
        verify(mockLogger).info(eq(expectedMessage));
    }

}