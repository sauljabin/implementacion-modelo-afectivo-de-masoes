/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import language.SemanticLanguage;
import masoes.ontology.MasoesOntology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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

public class NotifierAgentTest extends PowerMockitoTest {

    private NotifierAgent notifierAgent;
    private NotifierAgent notifierAgentSpy;
    private AgentLogger loggerMock;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        loggerMock = mock(AgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);
        notifierAgent = new NotifierAgent();
        setFieldValue(notifierAgent, "logger", loggerMock);
        setFieldValue(notifierAgent, "agentManagementAssistant", agentManagementAssistantMock);
        notifierAgentSpy = spy(notifierAgent);
    }

    @Test
    public void shouldAddBasicBehaviors() {
        notifierAgentSpy.setup();
        verify(notifierAgentSpy).addBehaviour(isA(NotifierBehaviour.class));
    }

    @Test
    public void shouldRegisterAgent() {
        notifierAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        testService(MasoesOntology.ACTION_NOTIFY_EVENT, 0);
        testService(MasoesOntology.ACTION_NOTIFY_ACTION, 1);
        testService(MasoesOntology.ACTION_NOTIFY_OBJECT, 2);
    }

    private void testService(String action, int index) {
        ServiceDescription serviceGetSetting = serviceDescriptionCaptor.getAllValues().get(index);
        assertThat(serviceGetSetting.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(serviceGetSetting.getAllOntologies().next(), is(MasoesOntology.NAME));
        assertThat(serviceGetSetting.getAllLanguages().next(), is(SemanticLanguage.NAME));
        assertThat(serviceGetSetting.getName(), is(action));
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class), any(ServiceDescription.class));
        try {
            notifierAgentSpy.setup();
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(expectedException);
        }
    }

}