/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import ontology.settings.SettingsOntology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class SettingsAgentTest {

    private SettingsAgent settingsAgentSpy;
    private AgentLogger loggerMock;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        loggerMock = mock(AgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);

        SettingsAgent settingsAgent = new SettingsAgent();
        setFieldValue(settingsAgent, "logger", loggerMock);
        setFieldValue(settingsAgent, "agentManagementAssistant", agentManagementAssistantMock);

        settingsAgentSpy = spy(settingsAgent);
    }

    @Test
    public void shouldAddSettingsBehaviour() {
        settingsAgentSpy.setup();
        verify(settingsAgentSpy).addBehaviour(isA(ResponseSettingsBehaviour.class));
    }

    @Test
    public void shouldRegisterAgent() throws Exception {
        settingsAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription serviceGetSetting = serviceDescriptionCaptor.getAllValues().get(0);
        assertThat(serviceGetSetting.getName(), is(SettingsOntology.ACTION_GET_SETTING));

        ServiceDescription serviceGetAllSettings = serviceDescriptionCaptor.getAllValues().get(1);
        assertThat(serviceGetAllSettings.getName(), is(SettingsOntology.ACTION_GET_ALL_SETTINGS));
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() throws Exception {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class));
        settingsAgentSpy.setup();
        verify(loggerMock).exception(settingsAgentSpy, expectedException);
    }

}