/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.agent;

import jade.content.ContentManager;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.language.FipaLanguage;
import jade.logger.JadeLogger;
import jade.settings.behaviour.ResponseSettingsBehaviour;
import jade.settings.ontology.SettingsOntology;
import jade.util.leap.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DFService.class, Agent.class})
public class SettingsAgentTest {

    private SettingsAgent spySettingsAgent;
    private JadeLogger mockLogger;
    private ArgumentCaptor<DFAgentDescription> agentDescriptionArgumentCaptor;
    private ArgumentCaptor<Behaviour> behaviourArgumentCaptor;
    private ArgumentCaptor<SettingsOntology> ontologyArgumentCaptor;
    private ArgumentCaptor<FipaLanguage> codecArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        agentDescriptionArgumentCaptor = ArgumentCaptor.forClass(DFAgentDescription.class);
        behaviourArgumentCaptor = ArgumentCaptor.forClass(Behaviour.class);
        ontologyArgumentCaptor = ArgumentCaptor.forClass(SettingsOntology.class);
        codecArgumentCaptor = ArgumentCaptor.forClass(FipaLanguage.class);
        mockLogger = mock(JadeLogger.class);

        SettingsAgent settingsAgent = new SettingsAgent();
        setFieldValue(settingsAgent, "logger", mockLogger);

        spySettingsAgent = spy(settingsAgent);
    }

    @Test
    public void shouldAddSettingsBehaviour() {
        spySettingsAgent.setup();
        verify(spySettingsAgent).addBehaviour(behaviourArgumentCaptor.capture());
        assertThat(behaviourArgumentCaptor.getValue(), is(instanceOf(ResponseSettingsBehaviour.class)));
    }

    @Test
    public void shouldAddOntologyAndLanguage() throws Exception {
        ContentManager mockContentManager = mock(ContentManager.class);
        when(spySettingsAgent.getContentManager()).thenReturn(mockContentManager);
        spySettingsAgent.setup();
        verify(mockContentManager).registerLanguage(codecArgumentCaptor.capture());
        verify(mockContentManager).registerOntology(ontologyArgumentCaptor.capture());
        assertThat(codecArgumentCaptor.getValue(), is(instanceOf(FipaLanguage.class)));
        assertThat(ontologyArgumentCaptor.getValue(), is(instanceOf(SettingsOntology.class)));
    }

    @Test
    public void shouldRegisterAgent() throws Exception {
        when(spySettingsAgent.getLocalName()).thenReturn("settings");

        mockStatic(DFService.class);
        spySettingsAgent.setup();
        verifyStatic();
        DFService.register(eq(spySettingsAgent), agentDescriptionArgumentCaptor.capture());

        Iterator allServices = agentDescriptionArgumentCaptor.getValue().getAllServices();

        ServiceDescription serviceGetSetting = (ServiceDescription) allServices.next();
        testService(serviceGetSetting, "GetSetting");

        ServiceDescription serviceGetAllSettings = (ServiceDescription) allServices.next();
        testService(serviceGetAllSettings, "GetAllSettings");
    }

    private void testService(ServiceDescription actualService, String name) {
        assertThat(actualService.getName(), is(name));
        assertThat(actualService.getType(), is("settings-" + name));
        assertThat(actualService.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actualService.getAllLanguages().next(), is(FipaLanguage.LANGUAGE_NAME));
        assertThat(actualService.getAllOntologies().next(), is(SettingsOntology.ONTOLOGY_NAME));
    }

    @Test
    public void shouldLogErrorWhenRegister() throws Exception {
        FIPAException expectedException = new FIPAException("error");
        mockStatic(DFService.class);
        doThrow(expectedException).when(DFService.class);
        DFService.register(eq(spySettingsAgent), any());
        spySettingsAgent.setup();
        verify(mockLogger).agentException(spySettingsAgent, expectedException);
    }

}