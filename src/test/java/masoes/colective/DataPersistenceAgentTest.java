/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import language.SemanticLanguage;
import ontology.masoes.MasoesOntology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class DataPersistenceAgentTest {

    private DataPersistenceAgent dataPersistenceAgentSpy;
    private AgentLogger loggerMock;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        loggerMock = mock(AgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);

        DataPersistenceAgent dataPersistenceAgent = new DataPersistenceAgent();
        setFieldValue(dataPersistenceAgent, "logger", loggerMock);
        setFieldValue(dataPersistenceAgent, "agentManagementAssistant", agentManagementAssistantMock);

        dataPersistenceAgentSpy = spy(dataPersistenceAgent);
    }

    @Test
    public void shouldAddSettingsBehaviour() {
        dataPersistenceAgentSpy.setup();
        verify(dataPersistenceAgentSpy).addBehaviour(isA(DataPersistenceBehaviour.class));
    }

    @Test
    public void shouldRegisterAgent() {
        dataPersistenceAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription createObject = serviceDescriptionCaptor.getAllValues().get(0);
        testService(createObject, MasoesOntology.ACTION_CREATE_OBJECT);

        ServiceDescription getObject = serviceDescriptionCaptor.getAllValues().get(1);
        testService(getObject, MasoesOntology.ACTION_GET_OBJECT);

        ServiceDescription updateObject = serviceDescriptionCaptor.getAllValues().get(2);
        testService(updateObject, MasoesOntology.ACTION_UPDATE_OBJECT);

        ServiceDescription deleteObject = serviceDescriptionCaptor.getAllValues().get(3);
        testService(deleteObject, MasoesOntology.ACTION_DELETE_OBJECT);
    }

    private void testService(ServiceDescription description, String name) {
        assertThat(description.getName(), is(name));
        assertThat(description.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(description.getAllOntologies().next(), is(MasoesOntology.NAME));
        assertThat(description.getAllLanguages().next(), is(SemanticLanguage.NAME));
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class), any(ServiceDescription.class), any(ServiceDescription.class));
        try {
            dataPersistenceAgentSpy.setup();
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(expectedException);
        }
    }

}