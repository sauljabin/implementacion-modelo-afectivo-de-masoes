/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.setting;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import masoes.app.logger.ApplicationLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DFService.class, Agent.class})
public class SettingsAgentTest {

    private SettingsAgent spySettingsAgent;
    private ApplicationLogger mockApplicationLogger;

    private ArgumentCaptor<DFAgentDescription> agentDescriptionArgumentCaptor = ArgumentCaptor.forClass(DFAgentDescription.class);

    @Before
    public void setUp() {
        mockApplicationLogger = mock(ApplicationLogger.class);
        spySettingsAgent = spy(new SettingsAgent(mockApplicationLogger));
        doNothing().when(spySettingsAgent).addBehaviour(any(SettingsBehaviour.class));
        doCallRealMethod().when(spySettingsAgent).setup();
    }

    @Test
    public void shouldAddSettingsBehaviour() {
        spySettingsAgent.setup();
        verify(spySettingsAgent).addBehaviour(any(SettingsBehaviour.class));
    }

    @Test
    public void shouldRegisterAgent() throws Exception {
        when(spySettingsAgent.getLocalName()).thenReturn("settings");

        mockStatic(DFService.class);
        spySettingsAgent.setup();
        verifyStatic();
        DFService.register(eq(spySettingsAgent), agentDescriptionArgumentCaptor.capture());

        ServiceDescription actualService = (ServiceDescription) agentDescriptionArgumentCaptor.getValue().getAllServices().next();

        assertThat(actualService.getName(), is("get-setting"));
        assertThat(actualService.getType(), is("settings-get-setting"));
        assertThat(actualService.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
    }

    @Test
    public void shouldLogErrorWhenRegister() throws Exception {
        FIPAException expectedException = new FIPAException("error");

        mockStatic(DFService.class);
        doThrow(expectedException).when(DFService.class);
        DFService.register(eq(spySettingsAgent), any());

        spySettingsAgent.setup();
        verify(mockApplicationLogger).agentException(spySettingsAgent, expectedException);
    }

}