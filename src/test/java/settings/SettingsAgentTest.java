/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
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
        doReturn("settings").when(settingsAgentSpy).getLocalName();

        settingsAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription serviceGetSetting = serviceDescriptionCaptor.getAllValues().get(0);
        testService(serviceGetSetting, "GetSetting");

        ServiceDescription serviceGetAllSettings = serviceDescriptionCaptor.getAllValues().get(1);
        testService(serviceGetAllSettings, "GetAllSettings");
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() throws Exception {
        FIPAException expectedException = new FIPAException("error");
        mockStatic(DFService.class);
        doThrow(expectedException).when(DFService.class);
        DFService.register(eq(settingsAgentSpy), any());
        settingsAgentSpy.setup();
        verify(loggerMock).exception(settingsAgentSpy, expectedException);
    }

    private void testService(ServiceDescription actualService, String name) {
        assertThat(actualService.getName(), is(name));
        assertThat(actualService.getType(), is("settings-" + name));
        assertThat(actualService.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actualService.getAllLanguages().next(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(actualService.getAllOntologies().next(), is(SettingsOntology.NAME));
    }

}