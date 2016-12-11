/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import masoes.app.logger.ApplicationLogger;
import masoes.app.settings.ApplicationSettings;
import masoes.jade.settings.JadeSettings;
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
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class ReplaySettingsBehaviourTest {

    private ApplicationSettings applicationSettings;
    private ReplaySettingsBehaviour spyReplaySettingsBehaviour;
    private Agent spyAgent;
    private ACLMessage mockAclMessageRequest;
    private ACLMessage mockAclMessageResponse;
    private ApplicationLogger mockLogger;
    private ObjectMapper objectMapper;
    private JadeSettings jadeSettings;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        applicationSettings = ApplicationSettings.getInstance();
        applicationSettings.load();

        jadeSettings = JadeSettings.getInstance();
        jadeSettings.load();

        spyAgent = spy(new Agent());
        mockLogger = mock(ApplicationLogger.class);

        ReplaySettingsBehaviour replaySettingsBehaviour = new ReplaySettingsBehaviour(spyAgent);
        setFieldValue(replaySettingsBehaviour, "logger", mockLogger);

        spyReplaySettingsBehaviour = spy(replaySettingsBehaviour);
        mockAclMessageRequest = mock(ACLMessage.class);
        mockAclMessageResponse = mock(ACLMessage.class);
        when(mockAclMessageRequest.createReply()).thenReturn(mockAclMessageResponse);
        when(spyAgent.receive(any())).thenReturn(mockAclMessageRequest);
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

        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("applicationSettings", applicationSettings.toMap());
        objectMap.put("jadeSettings", jadeSettings.toMap());

        String content = objectMapper.writeValueAsString(objectMap);

        verify(mockAclMessageResponse).setContent(content);
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(spyAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldSendSpecificSetting() {
        when(mockAclMessageRequest.getContent()).thenReturn("app.name");
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setContent(applicationSettings.get(ApplicationSettings.APP_NAME));
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(spyAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldSendSpecificJadeSetting() {
        jadeSettings.set("gui", "false");
        when(mockAclMessageRequest.getContent()).thenReturn("gui");
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setContent("false");
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(spyAgent).send(mockAclMessageResponse);
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