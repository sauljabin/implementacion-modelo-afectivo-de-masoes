/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import language.SemanticLanguage;
import org.junit.Test;
import test.FunctionalTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class AgentManagementAssistantFunctionalTest extends FunctionalTest {

    private static final String CONVERSATION_ID = "conversationID";
    private static final String AGENT_NAME = "agentName";

    @Test
    public void shouldCreateAndKillAgentWithoutWrapper() throws Exception {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

        request.addReceiver(getAMS());
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology(JADEManagementOntology.getInstance().getName());
        request.setConversationId(CONVERSATION_ID);
        request.setLanguage(SemanticLanguage.getInstance().getName());

        ContentManager contentManager = new ContentManager();
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        contentManager.registerOntology(JADEManagementOntology.getInstance());

        CreateAgent createAgent = new CreateAgent();
        createAgent.setAgentName(AGENT_NAME);
        createAgent.setClassName(ConfigurableAgent.class.getCanonicalName());
        createAgent.setContainer(getContainerID());

        Action actionCreateAgent = new Action(getAMS(), createAgent);

        contentManager.fillContent(request, actionCreateAgent);

        sendMessage(request);
        ACLMessage messageCreate = blockingReceive();
        assertThat(messageCreate.getConversationId(), is(CONVERSATION_ID));

        ContentElement contentElement = contentManager.extractContent(messageCreate);
        assertThat(contentElement, is(instanceOf(Done.class)));

        KillAgent killAgent = new KillAgent();
        killAgent.setAgent(getAID(AGENT_NAME));

        Action actionKillAgent = new Action(getAMS(), killAgent);

        contentManager.fillContent(request, actionKillAgent);

        sendMessage(request);
        ACLMessage messageKill = blockingReceive();
        assertThat(messageKill.getConversationId(), is(CONVERSATION_ID));

        contentElement = contentManager.extractContent(messageCreate);
        assertThat(contentElement, is(instanceOf(Done.class)));
    }

    @Test
    public void shouldCreateAndKillAgentWithAssistant() throws ControllerException {
        AID agent = createAgent(ConfigurableAgent.class, null);
        getAgent(agent.getLocalName());
        killAgent(agent);
        try {
            getAgent(agent.getLocalName());
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(agent.getName()));
        }
    }

}
