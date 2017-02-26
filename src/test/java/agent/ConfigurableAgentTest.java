/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import language.SemanticLanguage;
import ontology.configurable.ConfigurableOntology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ConfigurableAgentTest {

    private ConfigurableAgent configurableAgent;
    private ConfigurableAgent configurableAgentSpy;
    private AgentLogger loggerMock;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        loggerMock = mock(AgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);

        configurableAgent = new ConfigurableAgent();
        setFieldValue(configurableAgent, "logger", loggerMock);
        setFieldValue(configurableAgent, "agentManagementAssistant", agentManagementAssistantMock);

        configurableAgentSpy = spy(configurableAgent);
    }

    @Test
    public void shouldAddConfiguringAgentBehaviour() {
        configurableAgentSpy.setup();
        verify(configurableAgentSpy).addBehaviour(isA(ConfiguringAgentBehaviour.class));
    }

    @Test
    public void shouldRegisterAgent() throws Exception {
        configurableAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription addBehaviour = serviceDescriptionCaptor.getAllValues().get(0);
        testService(addBehaviour, ConfigurableOntology.ACTION_ADD_BEHAVIOUR);

        ServiceDescription removeBehaviour = serviceDescriptionCaptor.getAllValues().get(1);
        testService(removeBehaviour, ConfigurableOntology.ACTION_REMOVE_BEHAVIOUR);
    }

    private void testService(ServiceDescription description, String name) {
        assertThat(description.getName(), is(name));
        assertThat(description.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(description.getAllOntologies().next(), is(ConfigurableOntology.NAME));
        assertThat(description.getAllLanguages().next(), is(SemanticLanguage.NAME));
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() throws Exception {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class));
        try {
            configurableAgentSpy.setup();
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(configurableAgentSpy, expectedException);
        }
    }

}