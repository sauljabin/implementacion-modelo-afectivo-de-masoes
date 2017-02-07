/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
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
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class AgentManagementAssistantTest {

    private static final String AMS_NAME = "amsName";
    private static final String AGENT_NAME = "agentName";
    private static final String CONTAINER_NAME = "containerName";
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private AgentManagementAssistant agentManagementAssistant;
    private Agent agentMock;

    @Before
    public void setUp() {
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);

        agentMock = mock(Agent.class);
        doReturn(new AID(AGENT_NAME, AID.ISGUID)).when(agentMock).getAID();
        doReturn(new AID(AMS_NAME, AID.ISGUID)).when(agentMock).getAMS();
        doReturn(new ContainerID(CONTAINER_NAME, null)).when(agentMock).here();

        agentManagementAssistant = new AgentManagementAssistant(agentMock);
    }

    @Test
    public void shouldSendShutdownToAMSAgent() throws Exception {
        agentManagementAssistant.shutdownPlatform();
        ContentElement contentElement = testSendBasicMessageToAMSAgent();
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(ShutdownPlatform.class)));
    }

    @Test
    public void shouldSendKillContainerToAMSAgent() throws Exception {
        agentManagementAssistant.killContainer();
        ContentElement contentElement = testSendBasicMessageToAMSAgent();
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(KillContainer.class)));
        KillContainer killContainer = (KillContainer) action.getAction();
        assertThat(killContainer.getContainer().getName(), is(CONTAINER_NAME));
    }

    private ContentElement testSendBasicMessageToAMSAgent() throws Exception {
        verify(agentMock).send(messageArgumentCaptor.capture());

        ACLMessage message = messageArgumentCaptor.getValue();

        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(message.getOntology(), is(JADEManagementOntology.getInstance().getName()));
        assertThat(message.getLanguage(), is(SemanticLanguage.getInstance().getName()));
        assertThat(message.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(((AID) message.getAllReceiver().next()).getName(), is(AMS_NAME));
        assertThat(message.getSender().getName(), is(AGENT_NAME));

        ContentManager contentManager = new ContentManager();
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        contentManager.registerOntology(JADEManagementOntology.getInstance());

        ContentElement contentElement = contentManager.extractContent(message);
        assertThat(contentElement, is(instanceOf(Action.class)));
        return contentElement;
    }

    // TODO: TERMINAR PRUEBAS

}