/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import jade.JadeLogger;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.util.leap.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import settings.ontology.SettingsOntology;

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
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Agent.class, DFService.class})
public class SettingsAgentTest {

    private SettingsAgent settingsAgentSpy;
    private JadeLogger loggerMock;
    private ArgumentCaptor<DFAgentDescription> agentDescriptionArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        agentDescriptionArgumentCaptor = ArgumentCaptor.forClass(DFAgentDescription.class);
        loggerMock = mock(JadeLogger.class);

        SettingsAgent settingsAgent = new SettingsAgent();
        setFieldValue(settingsAgent, "logger", loggerMock);

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

        mockStatic(DFService.class);
        settingsAgentSpy.setup();
        verifyStatic();
        DFService.register(eq(settingsAgentSpy), agentDescriptionArgumentCaptor.capture());

        Iterator allServices = agentDescriptionArgumentCaptor.getValue().getAllServices();

        ServiceDescription serviceGetSetting = (ServiceDescription) allServices.next();
        testService(serviceGetSetting, "GetSetting");

        ServiceDescription serviceGetAllSettings = (ServiceDescription) allServices.next();
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
        assertThat(actualService.getAllOntologies().next(), is(SettingsOntology.ONTOLOGY_NAME));
    }

}