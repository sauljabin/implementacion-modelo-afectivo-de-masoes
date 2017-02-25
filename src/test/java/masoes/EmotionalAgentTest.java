/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import ontology.masoes.MasoesOntology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

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

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class EmotionalAgentTest {

    private static final String LOCAL_NAME = "localName";
    private EmotionalAgent emotionalAgentSpy;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;
    private EmotionalAgent emotionalAgent;
    private AgentLogger loggerMock;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        loggerMock = mock(AgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);
        emotionalAgent = createAgent();

        Field fieldLogger = emotionalAgent.getClass().getSuperclass().getDeclaredField("logger");
        fieldLogger.setAccessible(true);
        fieldLogger.set(emotionalAgent, loggerMock);

        Field fieldAssistant = emotionalAgent.getClass().getSuperclass().getDeclaredField("agentManagementAssistant");
        fieldAssistant.setAccessible(true);
        fieldAssistant.set(emotionalAgent, agentManagementAssistantMock);

        emotionalAgentSpy = spy(emotionalAgent);
        doReturn(LOCAL_NAME).when(emotionalAgentSpy).getLocalName();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        emotionalAgentSpy.setup();
        InOrder inOrder = inOrder(emotionalAgentSpy);
        inOrder.verify(emotionalAgentSpy).setUp();
        inOrder.verify(emotionalAgentSpy).addBehaviour(isA(BasicEmotionalAgentBehaviour.class));
    }

    @Test
    public void shouldLogErrorWhenRegisterThrowsException() throws Exception {
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class), any(ServiceDescription.class));
        try {
            emotionalAgentSpy.setup();
        } catch (Exception e) {
            verify(loggerMock).exception(emotionalAgentSpy, expectedException);
        }
    }

    @Test
    public void shouldCreateComponent() {
        emotionalAgentSpy.setup();
        assertThat(emotionalAgentSpy.getBehaviouralComponent(), is(notNullValue()));
    }

    @Test
    public void shouldRegisterAgent() throws Exception {
        emotionalAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription evaluate = serviceDescriptionCaptor.getAllValues().get(0);
        assertThat(evaluate.getName(), is(MasoesOntology.ACTION_EVALUATE_STIMULUS));

        ServiceDescription getState = serviceDescriptionCaptor.getAllValues().get(1);
        assertThat(getState.getName(), is(MasoesOntology.ACTION_GET_EMOTIONAL_STATE));
    }

    private EmotionalAgent createAgent() {
        return new EmotionalAgent() {

            @Override
            public void setUp() {
            }

            @Override
            public String getKnowledgePath() {
                return null;
            }

            @Override
            public ImitativeBehaviour getImitativeBehaviour() {
                return null;
            }

            @Override
            public ReactiveBehaviour getReactiveBehaviour() {
                return null;
            }

            @Override
            public CognitiveBehaviour getCognitiveBehaviour() {
                return null;
            }
        };
    }

}