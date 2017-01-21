/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.protocol;

import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.ontology.base.BaseOntology;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

public class OntologyResponderBehaviourTest {

    private static final String EXPECTED_EXCEPTION_MESSAGE = "EXCEPTION MESSAGE";
    private Agent agentMock;
    private OntologyResponderBehaviour ontologyResponderBehaviour;
    private ACLMessage request;
    private ContentManager contentManagerMock;
    private Action actionMock;
    private MessageTemplate messageTemplateMock;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        messageTemplateMock = mock(MessageTemplate.class);
        ontologyResponderBehaviour = spy(new OntologyResponderBehaviour(agentMock, messageTemplateMock));

        request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology(BaseOntology.ONTOLOGY_NAME);

        contentManagerMock = mock(ContentManager.class);
        doReturn(contentManagerMock).when(agentMock).getContentManager();

        actionMock = mock(Action.class);
        doReturn(actionMock).when(contentManagerMock).extractContent(request);

        doNothing().when(contentManagerMock).fillContent(any(), any());
    }

    @Test
    public void shouldReturnRefuseIfActionIsNotValid() throws Exception {
        doReturn(false).when(ontologyResponderBehaviour).isValidAction(actionMock);
        ACLMessage response = ontologyResponderBehaviour.prepareResponse(request);
        assertThat(response.getPerformative(), is(ACLMessage.REFUSE));
        assertThat(response.getContent(), is("Action no valid"));
    }

    @Test
    public void shouldReturnAgreeIfActionIsValid() throws Exception {
        doReturn(true).when(ontologyResponderBehaviour).isValidAction(actionMock);
        ACLMessage response = ontologyResponderBehaviour.prepareResponse(request);
        assertThat(response.getPerformative(), is(ACLMessage.AGREE));
    }

    @Test
    public void shouldReturnRefuseIfException() throws Exception {
        doThrow(new OntologyException(EXPECTED_EXCEPTION_MESSAGE)).when(contentManagerMock).extractContent(request);
        doReturn(true).when(ontologyResponderBehaviour).isValidAction(actionMock);
        ACLMessage response = ontologyResponderBehaviour.prepareResponse(request);
        assertThat(response.getPerformative(), is(ACLMessage.REFUSE));
        assertThat(response.getContent(), is(EXPECTED_EXCEPTION_MESSAGE));
    }

    @Test
    public void shouldReturnFailureIfException() throws Exception {
        doThrow(new RuntimeException(EXPECTED_EXCEPTION_MESSAGE)).when(ontologyResponderBehaviour).performAction(actionMock);
        ACLMessage response = ontologyResponderBehaviour.prepareResultNotification(request, request.createReply());
        assertThat(response.getPerformative(), is(ACLMessage.FAILURE));
        assertThat(response.getContent(), is(EXPECTED_EXCEPTION_MESSAGE));
    }

    @Test
    public void shouldReturnInformIfPerformAction() throws Exception {
        Predicate predicateMock = mock(Predicate.class);
        doReturn(predicateMock).when(ontologyResponderBehaviour).performAction(actionMock);
        ACLMessage response = request.createReply();
        response = ontologyResponderBehaviour.prepareResultNotification(request, response);
        assertThat(response.getPerformative(), is(ACLMessage.INFORM));
        verify(contentManagerMock).fillContent(response, predicateMock);
    }

}