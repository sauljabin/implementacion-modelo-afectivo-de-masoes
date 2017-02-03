/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import protocol.FillOntologyContentException;
import util.StringGenerator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class AgentMessageAssistantTest {

    private static final String RANDOM_STRING = "randomString";
    @Rule
    ExpectedException expectedException = ExpectedException.none();
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private ArgumentCaptor<ContentElement> contentElementArgumentCaptor;
    private AgentMessageAssistant agentMessageAssistant;
    private Agent agentMock;
    private AID aidAmsMock;
    private AID aidMock;
    private StringGenerator stringGeneratorMock;
    private ContentManager contentManagerMock;
    private ContainerID containerIdMock;

    @Before
    public void setUp() throws Exception {
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);
        contentElementArgumentCaptor = ArgumentCaptor.forClass(ContentElement.class);

        agentMock = mock(Agent.class);
        aidMock = mock(AID.class);
        aidAmsMock = mock(AID.class);
        stringGeneratorMock = mock(StringGenerator.class);
        contentManagerMock = mock(ContentManager.class);
        containerIdMock = mock(ContainerID.class);

        agentMessageAssistant = new AgentMessageAssistant(agentMock);

        doReturn(aidMock).when(agentMock).getAID();
        doReturn(aidAmsMock).when(agentMock).getAMS();
        doReturn(RANDOM_STRING).when(stringGeneratorMock).getString(anyInt());
        doReturn(containerIdMock).when(agentMock).here();

        setFieldValue(agentMessageAssistant, "stringGenerator", stringGeneratorMock);
        setFieldValue(agentMessageAssistant, "contentManager", contentManagerMock);
    }

    @Test
    public void shouldSendKillContainerMessage() throws Exception {
        agentMessageAssistant.killContainer();

        verify(stringGeneratorMock).getString(10);
        verify(contentManagerMock).fillContent(messageArgumentCaptor.capture(), contentElementArgumentCaptor.capture());
        verify(agentMock).send(eq(messageArgumentCaptor.getValue()));

        ACLMessage actual = messageArgumentCaptor.getValue();
        assertThat(actual.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL0));
        assertThat(actual.getOntology(), is(JADEManagementOntology.NAME));
        assertThat(actual.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(actual.getSender(), is(aidMock));
        assertThat(actual.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actual.getConversationId(), is(RANDOM_STRING));
        assertThat(actual.getAllReceiver().next(), is(aidAmsMock));

        ContentElement contentElement = contentElementArgumentCaptor.getValue();
        assertThat(contentElement, is(instanceOf(KillContainer.class)));

        KillContainer killContainer = (KillContainer) contentElement;
        assertThat(killContainer.getContainer(), is(containerIdMock));
    }

    @Test
    public void shouldThrowFillOntologyContentExceptionWhenErrorInContentManager() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new OntologyException(expectedMessage)).when(contentManagerMock).fillContent(any(ACLMessage.class), any(ContentElement.class));
        expectedException.expect(FillOntologyContentException.class);
        expectedException.expectMessage(expectedMessage);
        agentMessageAssistant.killContainer();
    }

}