/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.behaviour;

import application.logger.ApplicationLogger;
import application.settings.ApplicationSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.settings.JadeSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class ReplaySettingsBehaviourTest {

    private ApplicationSettings mockApplicationSettings;
    private ReplaySettingsBehaviour spyReplaySettingsBehaviour;
    private Agent spyAgent;
    private ACLMessage mockAclMessageRequest;
    private ACLMessage mockAclMessageResponse;
    private ApplicationLogger mockLogger;
    private ObjectMapper objectMapper;
    private JadeSettings mockJadeSettings;
    private Map<String, Object> expectedContent;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        mockApplicationSettings = mock(ApplicationSettings.class);
        mockJadeSettings = mock(JadeSettings.class);

        spyAgent = spy(new Agent());
        mockLogger = mock(ApplicationLogger.class);

        ReplaySettingsBehaviour replaySettingsBehaviour = new ReplaySettingsBehaviour(spyAgent);
        setFieldValue(replaySettingsBehaviour, "logger", mockLogger);
        setFieldValue(replaySettingsBehaviour, "applicationSettings", mockApplicationSettings);
        setFieldValue(replaySettingsBehaviour, "jadeSettings", mockJadeSettings);

        spyReplaySettingsBehaviour = PowerMockito.spy(replaySettingsBehaviour);
        mockAclMessageRequest = mock(ACLMessage.class);
        mockAclMessageResponse = mock(ACLMessage.class);
        when(mockAclMessageRequest.createReply()).thenReturn(mockAclMessageResponse);
        when(spyAgent.receive(any())).thenReturn(mockAclMessageRequest);

        Map<String, String> expectedApplicationSettingsMap = new HashMap<>();
        expectedApplicationSettingsMap.put("key1", "value1");
        when(mockApplicationSettings.toMap()).thenReturn(expectedApplicationSettingsMap);


        Map<String, String> expectedJadeSettingsMap = new HashMap<>();
        expectedJadeSettingsMap.put("key1", "value1");
        when(mockJadeSettings.toMap()).thenReturn(expectedJadeSettingsMap);

        expectedContent = new HashMap<>();
        expectedContent.put("applicationSettings", expectedApplicationSettingsMap);
        expectedContent.put("jadeSettings", expectedJadeSettingsMap);
    }

    @Test
    public void shouldBlockWhenMessageIsNull() {
        when(spyAgent.receive(any())).thenReturn(null);
        spyReplaySettingsBehaviour.action();
        verify(spyReplaySettingsBehaviour).block();
    }

    @Test
    public void shouldSendAllSettings() throws Exception {
        spyReplaySettingsBehaviour.action();

        String content = objectMapper.writeValueAsString(expectedContent);

        verify(mockAclMessageResponse).setContent(content);
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(spyAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldSendSpecificSetting() {
        String expectedContent = "content";
        when(mockApplicationSettings.get(ApplicationSettings.APP_NAME)).thenReturn(expectedContent);
        when(mockAclMessageRequest.getContent()).thenReturn(ApplicationSettings.APP_NAME);
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setContent(expectedContent);
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(spyAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldSendSpecificJadeSetting() {
        String expectedContent = "content";
        when(mockJadeSettings.get(JadeSettings.GUI)).thenReturn(expectedContent);
        when(mockAclMessageRequest.getContent()).thenReturn(JadeSettings.GUI);
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setContent(expectedContent);
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(spyAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldSendFailure() throws Exception {
        String expectedMessage = "";
        RuntimeException toBeThrown = new RuntimeException(expectedMessage);
        doThrow(toBeThrown).when(mockAclMessageResponse).setContent(objectMapper.writeValueAsString(expectedContent));
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setContent(expectedMessage);
        verify(mockAclMessageResponse).setPerformative(ACLMessage.FAILURE);
        verify(spyAgent).send(mockAclMessageResponse);
        verify(mockLogger).agentException(spyAgent, toBeThrown);
    }

    @Test
    public void shouldSendNotUnderstood() {
        when(mockAclMessageRequest.getContent()).thenReturn("no_key");
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setPerformative(ACLMessage.NOT_UNDERSTOOD);
        verify(spyAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldLogMessage() {
        spyReplaySettingsBehaviour.action();
        verify(mockLogger).agentMessage(spyAgent, mockAclMessageRequest);
    }

}