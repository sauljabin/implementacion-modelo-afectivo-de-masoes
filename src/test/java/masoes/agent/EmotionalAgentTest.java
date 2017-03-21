/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import agent.AgentManagementAssistant;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import language.SemanticLanguage;
import masoes.ontology.MasoesOntology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import test.PowerMockitoTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static test.ReflectionTestUtils.setFieldValue;

public class EmotionalAgentTest extends PowerMockitoTest {

    private static final String LOCAL_NAME = "localName";
    private EmotionalAgent emotionalAgentSpy;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;
    private EmotionalAgent emotionalAgent;
    private EmotionalAgentLogger loggerMock;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        loggerMock = mock(EmotionalAgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);
        emotionalAgent = createAgent();

        setFieldValue(emotionalAgent, "logger", loggerMock);
        setFieldValue(emotionalAgent, "agentManagementAssistant", agentManagementAssistantMock);

        emotionalAgentSpy = spy(emotionalAgent);
        doReturn(LOCAL_NAME).when(emotionalAgentSpy).getLocalName();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        emotionalAgentSpy.setup();
        InOrder inOrder = inOrder(emotionalAgentSpy);
        inOrder.verify(emotionalAgentSpy).addBehaviour(isA(EmotionalAgentBehaviour.class));
        inOrder.verify(emotionalAgentSpy).setUp();
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class));
        try {
            emotionalAgentSpy.setup();
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(expectedException);
        }
    }

    @Test
    public void shouldCreateComponent() {
        emotionalAgentSpy.setup();
        assertThat(emotionalAgentSpy.getBehaviouralComponent(), is(notNullValue()));
    }

    @Test
    public void shouldRegisterAgent() {
        emotionalAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription evaluate = serviceDescriptionCaptor.getAllValues().get(0);
        testService(evaluate, MasoesOntology.ACTION_EVALUATE_STIMULUS);

        ServiceDescription getState = serviceDescriptionCaptor.getAllValues().get(1);
        testService(getState, MasoesOntology.ACTION_GET_EMOTIONAL_STATE);
    }

    private void testService(ServiceDescription description, String name) {
        assertThat(description.getName(), is(name));
        assertThat(description.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(description.getAllOntologies().next(), is(MasoesOntology.NAME));
        assertThat(description.getAllLanguages().next(), is(SemanticLanguage.NAME));
    }

    private EmotionalAgent createAgent() {
        return new EmotionalAgent() {

            @Override
            public void setUp() {
            }
        };
    }

}