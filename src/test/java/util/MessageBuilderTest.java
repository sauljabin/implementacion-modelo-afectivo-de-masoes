/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import jade.content.lang.Codec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.TrueProposition;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
import ontology.FillOntologyContentException;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class MessageBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MessageBuilder messageBuilder;

    @Before
    public void setUp() {
        messageBuilder = new MessageBuilder();
    }

    @Test
    public void shouldSetSender() {
        AID sender = new AID();
        ACLMessage message = messageBuilder
                .sender(sender)
                .build();
        assertThat(message.getSender(), is(sender));
    }

    @Test
    public void shouldSetReceiver() {
        AID receiver = new AID();
        ACLMessage message = messageBuilder
                .receiver(receiver)
                .build();
        assertThat(message.getAllReceiver().next(), is(receiver));
    }

    @Test
    public void shouldSetPerformative() {
        ACLMessage message = messageBuilder
                .performative(ACLMessage.AGREE)
                .build();
        assertThat(message.getPerformative(), is(ACLMessage.AGREE));
    }

    @Test
    public void shouldSetRequestPerformative() {
        ACLMessage message = messageBuilder
                .performative(ACLMessage.AGREE)
                .request()
                .build();
        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
    }

    @Test
    public void shouldSetRequestPerformativeWhenNew() {
        ACLMessage message = messageBuilder.build();
        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
    }

    @Test
    public void shouldSetOntology() {
        Ontology ontologyMock = mock(Ontology.class);
        String expectedOntologyName = "expectedOntologyName";
        doReturn(expectedOntologyName).when(ontologyMock).getName();
        ACLMessage message = messageBuilder
                .ontology(ontologyMock)
                .build();
        assertThat(message.getOntology(), is(expectedOntologyName));
    }

    @Test
    public void shouldSetLanguage() {
        Codec languageMock = mock(Codec.class);
        String expectedLanguageName = "expectedLanguageName";
        doReturn(expectedLanguageName).when(languageMock).getName();
        ACLMessage message = messageBuilder
                .language(languageMock)
                .build();
        assertThat(message.getLanguage(), is(expectedLanguageName));
    }

    @Test
    public void shouldSetSemanticLanguage() {
        ACLMessage message = messageBuilder
                .fipaSL()
                .build();
        assertThat(message.getLanguage(), Is.is(SemanticLanguage.getInstance().getName()));
    }

    @Test
    public void shouldSetProtocol() {
        String expectedProtocol = "expectedProtocol";
        ACLMessage message = messageBuilder
                .protocol(expectedProtocol)
                .build();
        assertThat(message.getProtocol(), is(expectedProtocol));
    }

    @Test
    public void shouldSetRequestProtocol() {
        ACLMessage message = messageBuilder
                .fipaRequest()
                .build();
        assertThat(message.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
    }

    @Test
    public void shouldSetConversationId() {
        String expectedConversationId = "expectedConversationId";
        ACLMessage message = messageBuilder
                .conversationId(expectedConversationId)
                .build();
        assertThat(message.getConversationId(), is(expectedConversationId));
    }

    @Test
    public void shouldSetRandomConversationId() {
        ACLMessage message = messageBuilder
                .conversationId()
                .build();
        assertThat(message.getConversationId().length(), is(20));
    }

    @Test
    public void shouldSetReplyWith() {
        String expectedReplyWith = "expectedReplyWith";
        ACLMessage message = messageBuilder
                .replyWith(expectedReplyWith)
                .build();
        assertThat(message.getReplyWith(), is(expectedReplyWith));
    }

    @Test
    public void shouldSetRandomReplyWith() {
        ACLMessage message = messageBuilder
                .replyWith()
                .build();
        assertThat(message.getReplyWith().length(), is(20));
    }

    @Test
    public void shouldSetStringContent() {
        String expectedContent = "expectedContent";
        ACLMessage message = messageBuilder
                .content(expectedContent)
                .build();
        assertThat(message.getContent(), is(expectedContent));
    }

    @Test
    public void shouldFillContentWhenContentIsNotString() {
        ACLMessage message = messageBuilder
                .ontology(BasicOntology.getInstance())
                .fipaSL()
                .content(new TrueProposition())
                .build();
        assertThat(message.getContent(), is("(true)"));
    }

    @Test
    public void shouldThrowExceptionWhenOntologyIsEmpty() {
        expectedException.expectMessage("Null ontology registered");
        messageBuilder.fipaSL()
                .content(new TrueProposition())
                .build();
    }

    @Test
    public void shouldThrowExceptionWhenLanguageIsEmpty() {
        expectedException.expectMessage("Null codec registered");
        messageBuilder.ontology(BasicOntology.getInstance())
                .content(new TrueProposition())
                .build();
    }

    @Test
    public void shouldThrowFillOntologyContentExceptionWhenLanguageIsEmpty() {
        expectedException.expect(FillOntologyContentException.class);
        messageBuilder.ontology(BasicOntology.getInstance())
                .fipaSL()
                .content(new Done())
                .build();
    }

}