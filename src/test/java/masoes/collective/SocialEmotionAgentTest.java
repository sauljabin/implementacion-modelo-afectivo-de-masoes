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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static test.ReflectionTestUtils.setFieldValue;

public class SocialEmotionAgentTest {

    private SocialEmotionAgent socialEmotionAgentSpy;
    private AgentLogger loggerMock;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionCaptor;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionCaptor = ArgumentCaptor.forClass(ServiceDescription.class);
        loggerMock = mock(AgentLogger.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);

        SocialEmotionAgent socialEmotionAgent = new SocialEmotionAgent();
        setFieldValue(socialEmotionAgent, "logger", loggerMock);
        setFieldValue(socialEmotionAgent, "agentManagementAssistant", agentManagementAssistantMock);

        socialEmotionAgentSpy = spy(socialEmotionAgent);
    }

    @Test
    public void shouldAddSettingsBehaviour() {
        socialEmotionAgentSpy.setup();
        verify(socialEmotionAgentSpy).addBehaviour(isA(SocialEmotionBehaviour.class));
    }

    @Test
    public void shouldRegisterAgent() {
        socialEmotionAgentSpy.setup();

        verify(agentManagementAssistantMock).register(serviceDescriptionCaptor.capture());

        ServiceDescription createObject = serviceDescriptionCaptor.getAllValues().get(0);
        testService(createObject, MasoesOntology.ACTION_GET_SOCIAL_EMOTION);
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
        doThrow(expectedException).when(agentManagementAssistantMock).register(any(ServiceDescription.class));
        try {
            socialEmotionAgentSpy.setup();
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(expectedException);
        }
    }

}