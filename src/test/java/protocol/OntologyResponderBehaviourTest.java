/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class OntologyResponderBehaviourTest {

    private static final String EXPECTED_EXCEPTION_MESSAGE = "EXCEPTION MESSAGE";
    private Agent agentMock;
    private OntologyResponderBehaviour ontologyResponderBehaviour;
    private ACLMessage request;
    private ContentManager contentManagerMock;
    private Action actionMock;
    private MessageTemplate messageTemplateMock;
    private Ontology ontologyMock;
    private OntologyResponderBehaviour ontologyResponderBehaviourSpy;

    @Before
    public void setUp() throws Exception {
        request = new ACLMessage(ACLMessage.REQUEST);
        request.setOntology(BasicOntology.getInstance().getName());
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        agentMock = mock(Agent.class);
        messageTemplateMock = mock(MessageTemplate.class);
        ontologyMock = mock(Ontology.class);
        actionMock = mock(Action.class);
        contentManagerMock = mock(ContentManager.class);

        doReturn(actionMock).when(contentManagerMock).extractContent(request);
        doNothing().when(contentManagerMock).fillContent(any(), any());

        ontologyResponderBehaviour = new OntologyResponderBehaviour(agentMock, messageTemplateMock, ontologyMock);
        setFieldValue(ontologyResponderBehaviour, "contentManager", contentManagerMock);

        ontologyResponderBehaviourSpy = spy(ontologyResponderBehaviour);
    }

    @Test
    public void shouldConfigContentManager() throws Exception {
        String ontologyName = "ontologyName";
        doReturn(ontologyName).when(ontologyMock).getName();
        ontologyResponderBehaviour = new OntologyResponderBehaviour(agentMock, messageTemplateMock, ontologyMock);
        assertThat(ontologyResponderBehaviour.getContentManager().getOntologyNames()[0], is(ontologyName));
        assertThat(ontologyResponderBehaviour.getContentManager().getLanguageNames()[0], is(FIPANames.ContentLanguage.FIPA_SL));
    }

    @Test
    public void shouldReturnRefuseIfActionIsNotValid() throws Exception {
        doReturn(false).when(ontologyResponderBehaviourSpy).isValidAction(actionMock);
        ACLMessage response = ontologyResponderBehaviourSpy.prepareResponse(request);
        assertThat(response.getPerformative(), is(ACLMessage.REFUSE));
        assertThat(response.getContent(), is("Invalid action"));
    }

    @Test
    public void shouldReturnAgreeIfActionIsValid() throws Exception {
        doReturn(true).when(ontologyResponderBehaviourSpy).isValidAction(actionMock);
        ACLMessage response = ontologyResponderBehaviourSpy.prepareAcceptanceResponse(request);
        assertThat(response.getPerformative(), is(ACLMessage.AGREE));
    }

    @Test
    public void shouldReturnInformIfPerformAction() throws Exception {
        Predicate predicateMock = mock(Predicate.class);
        doReturn(predicateMock).when(ontologyResponderBehaviourSpy).performAction(actionMock);
        ACLMessage response = request.createReply();
        response = ontologyResponderBehaviourSpy.prepareInformResultResponse(request, response);
        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        verify(contentManagerMock).fillContent(response, predicateMock);
    }

    @Test
    public void shouldReturnNotUnderstoodIfThrowsExceptionInPrepareResponse() throws Exception {
        OntologyException toBeThrown = new OntologyException(EXPECTED_EXCEPTION_MESSAGE);
        doThrow(toBeThrown).when(contentManagerMock).extractContent(request);
        ACLMessage response = ontologyResponderBehaviourSpy.prepareResponse(request);
        assertThat(response.getPerformative(), is(ACLMessage.NOT_UNDERSTOOD));
        assertThat(response.getContent(), is(EXPECTED_EXCEPTION_MESSAGE));
    }

    @Test
    public void shouldReturnFailureIfThrowsExceptionInPerformAction() throws Exception {
        RuntimeException toBeThrown = new RuntimeException(EXPECTED_EXCEPTION_MESSAGE);
        doThrow(toBeThrown).when(ontologyResponderBehaviourSpy).performAction(actionMock);
        ACLMessage response = ontologyResponderBehaviourSpy.prepareResultNotification(request, request.createReply());
        assertThat(response.getPerformative(), is(ACLMessage.FAILURE));
        assertThat(response.getContent(), is(EXPECTED_EXCEPTION_MESSAGE));
    }

}