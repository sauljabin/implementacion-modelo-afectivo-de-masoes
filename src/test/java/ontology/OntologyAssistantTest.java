/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */


package ontology;

import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.TrueProposition;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import protocol.ProtocolAssistant;
import settings.ontology.GetAllSettings;
import settings.ontology.SettingsOntology;
import test.PowerMockitoTest;
import util.MessageBuilder;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class OntologyAssistantTest extends PowerMockitoTest {

    private static final String CONTENT = "((action (agent-identifier :name \"\") (GetAllSettings)))";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private OntologyAssistant ontologyAssistant;
    private AID aidMock;
    private Agent agentMock;
    private AID receiver;
    private ProtocolAssistant protocolAssistantMock;

    @Before
    public void setUp() throws Exception {
        receiver = new AID();

        agentMock = mock(Agent.class);
        aidMock = mock(AID.class);
        protocolAssistantMock = mock(ProtocolAssistant.class);

        doReturn(aidMock).when(agentMock).getAID();

        ontologyAssistant = new OntologyAssistant(agentMock, SettingsOntology.getInstance());

        setFieldValue(ontologyAssistant, "protocolAssistant", protocolAssistantMock);
    }

    @Test
    public void shouldCreateActionAndFillContent() {
        GetAllSettings content = new GetAllSettings();
        ACLMessage actualMessage = ontologyAssistant.createRequestAction(receiver, content);

        assertThat(actualMessage.getAllReceiver().next(), is(receiver));
        assertThat(actualMessage.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(actualMessage.getSender(), is(aidMock));
        assertThat(actualMessage.getOntology(), is(SettingsOntology.getInstance().getName()));
        assertThat(actualMessage.getLanguage(), is(SemanticLanguage.getInstance().getName()));
        assertThat(actualMessage.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actualMessage.getReplyWith().length(), is(20));
        assertThat(actualMessage.getConversationId().length(), is(20));
        assertThat(actualMessage.getContent(), is(CONTENT));
    }

    @Test
    public void shouldExtractContent() {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setContent(CONTENT);
        message.setLanguage(SemanticLanguage.getInstance().getName());
        message.setOntology(SettingsOntology.getInstance().getName());
        ContentElement contentElement = ontologyAssistant.extractMessageContent(message);
        assertThat(contentElement, is(instanceOf(Action.class)));

        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(GetAllSettings.class)));
    }

    @Test
    public void shouldThrowExtractContentException() {
        expectedException.expect(ExtractOntologyContentException.class);
        ontologyAssistant.extractMessageContent(new ACLMessage(ACLMessage.REQUEST));
    }

    @Test
    public void shouldFillContentWhenContentIsNotString() {
        ACLMessage message = new MessageBuilder()
                .ontology(SettingsOntology.getInstance())
                .fipaSL()
                .build();

        ontologyAssistant.fillMessageContent(message, new TrueProposition());
        assertThat(message.getContent(), is("(true)"));
    }

    @Test
    public void shouldThrowFillContentException() {
        expectedException.expect(FillOntologyContentException.class);
        ontologyAssistant.fillMessageContent(new ACLMessage(ACLMessage.REQUEST), new Done());
    }

    @Test
    public void shouldSendRequestActionWithProtocolAssistant() {
        doReturn(new MessageBuilder()
                .request()
                .sender(receiver)
                .receiver(receiver)
                .ontology(SettingsOntology.getInstance())
                .fipaSL()
                .fipaRequest()
                .conversationId()
                .replyWith()
                .content(new Done(new Action(receiver, new GetAllSettings())))
                .build()).when(protocolAssistantMock).sendRequest(any(ACLMessage.class), eq(ACLMessage.INFORM));
        ontologyAssistant.sendRequestAction(receiver, new GetAllSettings());
        verify(protocolAssistantMock).sendRequest(any(ACLMessage.class), eq(ACLMessage.INFORM));
    }

}