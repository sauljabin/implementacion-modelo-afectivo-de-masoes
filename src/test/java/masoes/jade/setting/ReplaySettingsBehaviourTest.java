/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.setting;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import masoes.app.logger.ApplicationLogger;
import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class ReplaySettingsBehaviourTest {

    private SettingsLoader settingsLoader = SettingsLoader.getInstance();
    private ReplaySettingsBehaviour spyReplaySettingsBehaviour;
    private Agent spyAgent;
    private ACLMessage mockAclMessageRequest;
    private ACLMessage mockAclMessageResponse;
    private ApplicationLogger mockApplicationLogger;

    @Before
    public void setUp() {
        settingsLoader.load();
        spyAgent = spy(new Agent());
        mockApplicationLogger = mock(ApplicationLogger.class);
        spyReplaySettingsBehaviour = spy(new ReplaySettingsBehaviour(spyAgent, mockApplicationLogger));
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
    public void shouldSendAllSettings() {
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setContent(Setting.allToString());
        verify(mockAclMessageResponse).setPerformative(ACLMessage.INFORM);
        verify(spyAgent).send(mockAclMessageResponse);
    }

    @Test
    public void shouldSendSpecificSetting() {
        when(mockAclMessageRequest.getContent()).thenReturn(Setting.APP_NAME.getKey());
        spyReplaySettingsBehaviour.action();
        verify(mockAclMessageResponse).setContent(Setting.APP_NAME.getValue());
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
        verify(mockApplicationLogger).agentMessage(spyAgent, mockAclMessageRequest);
    }

}