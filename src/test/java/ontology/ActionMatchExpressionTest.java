/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.stimulus.EvaluateStimulus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class ActionMatchExpressionTest {

    private ACLMessage message;
    private static final String EXPECTED_ONTOLOGY = "expectedOntology";
    private Ontology ontologyMock;
    private ActionMatchExpression actionMatchExpression;
    private ContentManager contentManagerMock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        contentManagerMock = mock(ContentManager.class);

        ontologyMock = mock(Ontology.class);
        doReturn(EXPECTED_ONTOLOGY).when(ontologyMock).getName();

        actionMatchExpression = new ActionMatchExpression(ontologyMock, GetEmotionalState.class);
        setFieldValue(actionMatchExpression, "contentManager", contentManagerMock);

        message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setOntology(EXPECTED_ONTOLOGY);
    }

    @Test
    public void shouldReturnTrueWhenIsCorrectAction() throws Exception {
        doReturn(new GetEmotionalState()).when(contentManagerMock).extractContent(message);
        assertTrue(actionMatchExpression.match(message));
    }

    @Test
    public void shouldReturnFalseWhenIsNotCorrectAction() throws Exception {
        doReturn(new EvaluateStimulus()).when(contentManagerMock).extractContent(message);
        assertFalse(actionMatchExpression.match(message));
    }

    @Test
    public void shouldReturnFalseWhenIsNotCorrectOntology() throws Exception {
        doReturn(new GetEmotionalState()).when(contentManagerMock).extractContent(message);
        message.setOntology("incorrectOntology");
        assertFalse(actionMatchExpression.match(message));
    }

    @Test
    public void shouldThrowExceptionWhenContentManagerThrowError() throws Exception {
        expectedException.expect(ExtractOntologyContentException.class);

        doThrow(new OntologyException("message")).when(contentManagerMock).extractContent(message);
        actionMatchExpression.match(message);
    }

}