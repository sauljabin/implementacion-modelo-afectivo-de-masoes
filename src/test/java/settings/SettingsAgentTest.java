/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import language.SemanticLanguage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import settings.ontology.SettingsOntology;
import test.PowerMockitoTest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class SettingsAgentTest extends PowerMockitoTest {

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
    public void shouldRegisterAgent() {
        settingsAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription serviceGetSetting = serviceDescriptionCaptor.getAllValues().get(0);
        testService(serviceGetSetting, SettingsOntology.ACTION_GET_SETTING);

        ServiceDescription serviceGetAllSettings = serviceDescriptionCaptor.getAllValues().get(1);
        testService(serviceGetAllSettings, SettingsOntology.ACTION_GET_ALL_SETTINGS);
    }

    private void testService(ServiceDescription description, String name) {
        assertThat(description.getName(), is(name));
        assertThat(description.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(description.getAllOntologies().next(), is(SettingsOntology.NAME));
        assertThat(description.getAllLanguages().next(), is(SemanticLanguage.NAME));
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class));
        try {
            settingsAgentSpy.setup();
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(expectedException);
        }
    }

}