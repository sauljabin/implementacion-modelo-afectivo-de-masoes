/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import agent.AgentLogger;
import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import test.PowerMockitoTest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class OntologyRequesterBehaviourTest extends PowerMockitoTest {

    private static final String ONTOLOGY_NAME = "ONTOLOGY NAME";
    private static final String MESSAGE = "MESSAGE";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private OntologyRequesterBehaviour ontologyRequesterBehaviour;
    private Agent agentMock;
    private AgentAction agentActionMock;
    private ACLMessage request;
    private Ontology ontologyMock;
    private AID receiverMock;
    private ContentManager contentManagerMock;
    private AID senderMock;
    private ArgumentCaptor<Action> actionArgumentCaptor;
    private OntologyRequesterBehaviour ontologyRequesterBehaviourSpy;
    private ACLMessage requestMock;
    private AgentLogger loggerMock;

    @Before
    public void setUp() throws Exception {
        actionArgumentCaptor = ArgumentCaptor.forClass(Action.class);

        agentMock = mock(Agent.class);
        agentActionMock = mock(AgentAction.class);
        ontologyMock = mock(Ontology.class);
        receiverMock = mock(AID.class);
        senderMock = mock(AID.class);
        requestMock = mock(ACLMessage.class);
        loggerMock = mock(AgentLogger.class);

        ontologyRequesterBehaviour = new OntologyRequesterBehaviour(agentMock, receiverMock, ontologyMock, agentActionMock);
        request = new ACLMessage(ACLMessage.REQUEST);

        doReturn(ONTOLOGY_NAME).when(ontologyMock).getName();
        doReturn(senderMock).when(agentMock).getAID();

        contentManagerMock = mock(ContentManager.class);
        setFieldValue(ontologyRequesterBehaviour, "contentManager", contentManagerMock);
        setFieldValue(ontologyRequesterBehaviour, "logger", loggerMock);

        ontologyRequesterBehaviourSpy = spy(ontologyRequesterBehaviour);
    }

    @Test
    public void shouldPrepareCorrectRequestMessage() throws Exception {
        String expectedConversationId = "string";

        ACLMessage actualRequest = ontologyRequesterBehaviour.prepareRequestInteraction(request);
        verify(contentManagerMock).registerOntology(ontologyMock);
        verify(contentManagerMock).registerLanguage(isA(SLCodec.class));
        verify(contentManagerMock).fillContent(eq(request), actionArgumentCaptor.capture());
        assertThat(actionArgumentCaptor.getValue().getActor(), is(senderMock));
        assertThat(actionArgumentCaptor.getValue().getAction(), is(agentActionMock));
        assertThat(actualRequest.getSender(), is(senderMock));
        assertThat(actualRequest.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(actualRequest.getConversationId(), is(notNullValue()));
        assertThat(actualRequest.getOntology(), is(ONTOLOGY_NAME));
        assertThat(actualRequest.getLanguage(), is(FIPANames.ContentLanguage.FIPA_SL));
        assertThat(actualRequest.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(actualRequest.getAllReceiver().next(), is(receiverMock));
    }

    @Test
    public void shouldThrowExceptionWhenFillContentThrowsAException() throws Exception {
        OntologyException toBeThrown = new OntologyException(MESSAGE);
        doThrow(toBeThrown).when(contentManagerMock).fillContent(eq(request), any(AgentAction.class));
        expectedException.expectMessage(MESSAGE);
        expectedException.expect(FillOntologyContentException.class);
        ontologyRequesterBehaviour.prepareRequestInteraction(request);
    }

    @Test
    public void shouldLogExceptionWhenFillContentThrowsAException() throws Exception {
        OntologyException toBeThrown = new OntologyException(MESSAGE);
        doThrow(toBeThrown).when(contentManagerMock).fillContent(eq(request), any(AgentAction.class));
        try {
            ontologyRequesterBehaviour.prepareRequestInteraction(request);
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(eq(toBeThrown));
        }
    }

    @Test
    public void shouldInvokeHandleAgreeWithContentMessage() {
        doReturn(MESSAGE).when(requestMock).getContent();
        ontologyRequesterBehaviourSpy.handleAgree(requestMock);
        verify(ontologyRequesterBehaviourSpy).handleAgree(MESSAGE);
    }

    @Test
    public void shouldInvokeHandleRefuseWithContentMessage() {
        doReturn(MESSAGE).when(requestMock).getContent();
        ontologyRequesterBehaviourSpy.handleRefuse(requestMock);
        verify(ontologyRequesterBehaviourSpy).handleRefuse(MESSAGE);
    }

    @Test
    public void shouldInvokeHandleFailureWithContentMessage() {
        doReturn(MESSAGE).when(requestMock).getContent();
        ontologyRequesterBehaviourSpy.handleFailure(requestMock);
        verify(ontologyRequesterBehaviourSpy).handleFailure(MESSAGE);
    }

    @Test
    public void shouldInvokeHandleNotUnderstoodWithContentMessage() {
        doReturn(MESSAGE).when(requestMock).getContent();
        ontologyRequesterBehaviourSpy.handleNotUnderstood(requestMock);
        verify(ontologyRequesterBehaviourSpy).handleNotUnderstood(MESSAGE);
    }

    @Test
    public void shouldInvokeHandleOutOfSequenceWithContentMessage() {
        doReturn(MESSAGE).when(requestMock).getContent();
        ontologyRequesterBehaviourSpy.handleOutOfSequence(requestMock);
        verify(ontologyRequesterBehaviourSpy).handleOutOfSequence(MESSAGE);
    }

    @Test
    public void shouldInvokeHandleInformWithPredicate() throws Exception {
        Predicate predicateMock = mock(Predicate.class);
        doReturn(predicateMock).when(contentManagerMock).extractContent(requestMock);
        ontologyRequesterBehaviourSpy.handleInform(requestMock);
        verify(ontologyRequesterBehaviourSpy).handleInform(predicateMock);
    }

    @Test
    public void shouldThrowExceptionWhenExtractContentThrowsAException() throws Exception {
        OntologyException toBeThrown = new OntologyException(MESSAGE);
        doThrow(toBeThrown).when(contentManagerMock).extractContent(request);
        expectedException.expectMessage(MESSAGE);
        expectedException.expect(ExtractOntologyContentException.class);
        ontologyRequesterBehaviour.handleInform(request);
    }

    @Test
    public void shouldLogExceptionWhenExtractContentThrowsAException() throws Exception {
        OntologyException toBeThrown = new OntologyException(MESSAGE);
        doThrow(toBeThrown).when(contentManagerMock).extractContent(request);
        try {
            ontologyRequesterBehaviour.handleInform(request);
        } catch (Exception e) {
        } finally {
            verify(loggerMock).exception(eq(toBeThrown));
        }
    }

}