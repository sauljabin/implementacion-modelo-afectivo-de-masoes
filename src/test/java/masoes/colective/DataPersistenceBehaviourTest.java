/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import data.DataBaseConnection;
import data.QueryResult;
import jade.content.Concept;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import ontology.OntologyMatchExpression;
import ontology.masoes.CreateObject;
import ontology.masoes.DeleteObject;
import ontology.masoes.GetObject;
import ontology.masoes.ListObjects;
import ontology.masoes.MasoesOntology;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
import ontology.masoes.UpdateObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import test.PowerMockitoTest;

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static test.ReflectionTestUtils.setFieldValue;

public class DataPersistenceBehaviourTest extends PowerMockitoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private DataPersistenceBehaviour dataPersistenceBehaviour;
    private Agent agentMock;
    @Mock
    private DataBaseConnection dataBaseConnection;
    private byte counter = 0;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        dataPersistenceBehaviour = new DataPersistenceBehaviour(agentMock);
        setFieldValue(dataPersistenceBehaviour, "connection", dataBaseConnection);
        counter = 0;
    }

    @Test
    public void shouldUseTransactionsWhenPerformingActions() throws Exception {
        mockEverything();
        Action action = new Action(new AID(), randomModifyAction());
        dataPersistenceBehaviour.performAction(action);
        InOrder orderVerifier = inOrder(dataBaseConnection);
        orderVerifier.verify(dataBaseConnection).beginTransaction();
        orderVerifier.verify(dataBaseConnection).endTransaction();
    }

    @Test
    public void shouldRollbackTransactionIfException() {
        QueryResult queryResult = mockEverything();
        when(dataBaseConnection.execute(anyString())).thenReturn(false);
        when(queryResult.next()).thenReturn(false);

        try {
            dataPersistenceBehaviour.performAction(new Action(new AID(), randomModifyAction()));
            fail();
        } catch (FailureException e) {
            InOrder orderVerifier = inOrder(dataBaseConnection);
            orderVerifier.verify(dataBaseConnection).beginTransaction();
            orderVerifier.verify(dataBaseConnection).rollbackTransaction();
        }
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action getObject = new Action(new AID(), new GetObject());
        Action createObject = new Action(new AID(), new CreateObject());
        Action deleteObject = new Action(new AID(), new DeleteObject());
        Action updateObject = new Action(new AID(), new UpdateObject());
        assertTrue(dataPersistenceBehaviour.isValidAction(getObject));
        assertTrue(dataPersistenceBehaviour.isValidAction(createObject));
        assertTrue(dataPersistenceBehaviour.isValidAction(deleteObject));
        assertTrue(dataPersistenceBehaviour.isValidAction(updateObject));
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(dataPersistenceBehaviour.getOntology(), is(instanceOf(MasoesOntology.class)));
        assertReflectionEquals(new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), dataPersistenceBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldCreateObjectWithProperties() throws Exception {
        when(dataBaseConnection.execute(anyString())).thenReturn(true);

        Action createObject = new Action(new AID(), new CreateObject(createObjectStimulus()));
        dataPersistenceBehaviour.performAction(createObject);

        String expectedInsertObjectSQL = "INSERT INTO object \\(uuid, name, creator_name\\) VALUES \\(\\'.*\\', \\'expectedObjectName\\', \\'expectedAgentName\\'\\);";
        String expectedInsertObjectPropertySQL = "INSERT INTO object_property \\(object_uuid, name, value\\) VALUES \\(\\'.*\\', \\'expectedPropertyName\\', \\'expectedPropertyValue\\'\\);";
        String expectedInsertObjectProperty2SQL = "INSERT INTO object_property \\(object_uuid, name, value\\) VALUES \\(\\'.*\\', \\'expectedPropertyName2\\', \\'expectedPropertyValue2\\'\\);";
        verify(dataBaseConnection).execute(matches(expectedInsertObjectSQL));
        verify(dataBaseConnection).execute(matches(expectedInsertObjectPropertySQL));
        verify(dataBaseConnection).execute(matches(expectedInsertObjectProperty2SQL));
    }

    @Test
    public void shouldThrowExceptionIfNoAffectedRowsWhenInserting() throws Exception {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Operation failed");
        when(dataBaseConnection.execute(anyString())).thenAnswer(answerRandomlyWithAtLeastOneFalse());

        Action createObject = new Action(new AID(), new CreateObject(createObjectStimulus()));
        dataPersistenceBehaviour.performAction(createObject);
    }

    @Test
    public void shouldCreateObjectWithoutPropertiesIfPropertiesListIsNull() throws Exception {
        when(dataBaseConnection.execute(anyString())).thenReturn(true);

        ObjectStimulus objectStimulus = createObjectStimulus();
        objectStimulus.setObjectProperties(null);
        Action createObject = new Action(new AID(), new CreateObject(objectStimulus));
        dataPersistenceBehaviour.performAction(createObject);

        String expectedInsertObjectSQL = "INSERT INTO object \\(uuid, name, creator_name\\) VALUES \\(\\'.*\\', \\'expectedObjectName\\', \\'expectedAgentName\\'\\);";
        String unexpectedInsertObjectPropertySQL = "INSERT INTO object_property \\(object_uuid, name, value\\) VALUES \\(\\'.*\\', \\'expectedPropertyName\\', \\'expectedPropertyValue\\'\\);";
        String unexpectedInsertObjectProperty2SQL = "INSERT INTO object_property \\(object_uuid, name, value\\) VALUES \\(\\'.*\\', \\'expectedPropertyName2\\', \\'expectedPropertyValue2\\'\\);";
        verify(dataBaseConnection).execute(matches(expectedInsertObjectSQL));
        verify(dataBaseConnection, never()).execute(matches(unexpectedInsertObjectPropertySQL));
        verify(dataBaseConnection, never()).execute(matches(unexpectedInsertObjectProperty2SQL));
    }

    @Test
    public void shouldRetrieveObjectListByUsingDataBaseConnection() throws Exception {
        QueryResult queryResult = mock(QueryResult.class);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(queryResult.next()).thenAnswer(answerTrueTwice());
        when(queryResult.getString("name")).thenReturn("expectedObjectName");
        when(queryResult.getString("creator_name")).thenReturn("expectedAgentName");
        when(queryResult.getString("uuid")).thenReturn("adbce68549");
        when(queryResult.getString("value")).thenReturn("value");
        AID agentAID = mock(AID.class);
        when(agentMock.getAID("expectedAgentName")).thenReturn(agentAID);

        ObjectStimulus objectStimulus = createObjectStimulus();
        objectStimulus.setObjectProperties(null);
        Action getObject = new Action(agentAID, new GetObject(objectStimulus));

        ObjectStimulus actualObject = (ObjectStimulus) ((ListObjects) dataPersistenceBehaviour.performAction(getObject)).getObjects().get(0);
        ObjectProperty actualProperty = (ObjectProperty) actualObject.getObjectProperties().get(0);

        assertThat(actualObject.getObjectName(), is("expectedObjectName"));
        assertThat(actualObject.getCreator(), is(agentAID));
        assertThat(actualProperty.getName(), is("expectedObjectName"));
        assertThat(actualProperty.getValue(), is("value"));
    }

    @Test
    public void shouldRetrieveObjectByUsingDataBase() throws Exception {
        QueryResult queryResult = mock(QueryResult.class);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(queryResult.next()).thenAnswer(answerTrueTwice());
        when(queryResult.getString(anyString())).thenReturn("");
        when(agentMock.getAID(anyString())).thenReturn(mock(AID.class));

        ObjectStimulus objectStimulus = createObjectStimulus();
        objectStimulus.setObjectProperties(null);
        Action getObject = new Action(null, new GetObject(objectStimulus));

        dataPersistenceBehaviour.performAction(getObject);

        String expectedObjectQuery = "SELECT name, creator_name, uuid FROM object WHERE object.name LIKE 'expectedObjectName' AND object.creator_name LIKE 'expectedAgentName';";
        String expectedObjectPropertySQL = "SELECT name, value FROM object_property WHERE object_uuid LIKE '';";
        verify(dataBaseConnection).query(expectedObjectQuery);
        verify(dataBaseConnection).query(expectedObjectPropertySQL);
    }

    @Test
    public void shouldThrowExceptionWhenNoObjectWasFound() throws Exception {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("No such object: expectedObjectName");

        QueryResult queryResult = mock(QueryResult.class);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(queryResult.next()).thenReturn(false);

        ObjectStimulus objectStimulus = createObjectStimulus();
        objectStimulus.setObjectProperties(null);
        Action getObject = new Action(null, new GetObject(objectStimulus));

        dataPersistenceBehaviour.performAction(getObject);
    }

    @Test
    public void shouldDeleteObjectAndItsProperties() throws Exception {
        QueryResult queryResult = mock(QueryResult.class);
        when(dataBaseConnection.execute(anyString())).thenReturn(true);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(queryResult.getString("uuid")).thenReturn("123");
        when(queryResult.next()).thenReturn(true);

        ObjectStimulus objectStimulus = createObjectStimulus();
        Action deleteObject = new Action(new AID(), new DeleteObject(objectStimulus));
        dataPersistenceBehaviour.performAction(deleteObject);

        String expectedQuery = "SELECT uuid FROM object WHERE name LIKE 'expectedObjectName' AND creator_name LIKE 'expectedAgentName';";
        String expectedDeleteObjectPropertySQL = "DELETE FROM object_property WHERE object_uuid LIKE '123';";
        String expectedDeleteObjectSQL = "DELETE FROM object WHERE uuid LIKE '123';";
        verify(dataBaseConnection).query(expectedQuery);
        verify(dataBaseConnection).execute(expectedDeleteObjectPropertySQL);
        verify(dataBaseConnection).execute(expectedDeleteObjectSQL);
    }

    @Test
    public void shouldThrowExceptionIfObjectWasNotFound() throws Exception {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("No such object: expectedObjectName");

        QueryResult queryResult = mock(QueryResult.class);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(queryResult.next()).thenReturn(false);

        ObjectStimulus objectStimulus = createObjectStimulus();
        Action deleteObject = new Action(new AID(), new DeleteObject(objectStimulus));
        dataPersistenceBehaviour.performAction(deleteObject);
    }

    @Test
    public void shouldUpdateObjectByResetingProperties() throws Exception {
        QueryResult queryResult = mock(QueryResult.class);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(dataBaseConnection.execute(anyString())).thenReturn(true);
        when(queryResult.getString("uuid")).thenReturn("123");
        when(queryResult.next()).thenReturn(true);

        ObjectStimulus objectStimulus = createObjectStimulus();
        Action updateObject = new Action(new AID(), new UpdateObject(objectStimulus));
        dataPersistenceBehaviour.performAction(updateObject);

        String expectedQuery = "SELECT uuid FROM object WHERE name LIKE 'expectedObjectName' AND creator_name LIKE 'expectedAgentName';";
        String expectedUpdateObjectPropertySQL = "DELETE FROM object_property WHERE object_uuid LIKE '123';";
        String expectedUpdateObjectSQL = "INSERT INTO object_property (object_uuid, name, value) VALUES ('123', 'expectedPropertyName', 'expectedPropertyValue');";
        verify(dataBaseConnection).query(expectedQuery);
        verify(dataBaseConnection).execute(expectedUpdateObjectPropertySQL);
        verify(dataBaseConnection).execute(expectedUpdateObjectSQL);
    }

    @Test
    public void shouldThrowExceptionIfUpdateObjectWasNotFound() throws Exception {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("No such object: expectedObjectName");

        QueryResult queryResult = mock(QueryResult.class);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(queryResult.next()).thenReturn(false);

        ObjectStimulus objectStimulus = createObjectStimulus();
        Action updateObject = new Action(new AID(), new UpdateObject(objectStimulus));
        dataPersistenceBehaviour.performAction(updateObject);
    }

    @Test
    public void shouldThrowExceptionIfNoAffectedRowsWhenInsertingUpdatedProperties() throws Exception {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Operation failed");
        QueryResult queryResult = mock(QueryResult.class);
        when(queryResult.next()).thenReturn(true);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        when(dataBaseConnection.execute(anyString())).thenAnswer(answerTrueOnce());

        ObjectStimulus objectStimulus = createObjectStimulus();
        Action updateObject = new Action(new AID(), new UpdateObject(objectStimulus));
        dataPersistenceBehaviour.performAction(updateObject);
    }

    private ObjectStimulus createObjectStimulus() {
        String expectedObjectName = "expectedObjectName";
        String expectedAgentName = "expectedAgentName";

        String expectedPropertyValue = "expectedPropertyValue";
        String expectedPropertyValue2 = "expectedPropertyValue2";
        String expectedPropertyName = "expectedPropertyName";
        String expectedPropertyName2 = "expectedPropertyName2";

        AID creator = new AID(expectedAgentName, AID.ISGUID);

        ArrayList objectProperties = new ArrayList();
        objectProperties.add(new ObjectProperty(expectedPropertyName, expectedPropertyValue));
        objectProperties.add(new ObjectProperty(expectedPropertyName2, expectedPropertyValue2));

        ObjectStimulus objectStimulus = new ObjectStimulus();
        objectStimulus.setObjectName(expectedObjectName);
        objectStimulus.setCreator(creator);
        objectStimulus.setObjectProperties(objectProperties);

        return objectStimulus;
    }

    private Answer<Boolean> answerTrueOnce() {
        return invocation -> {
            counter++;
            return counter < 2;
        };
    }

    private Answer<Boolean> answerTrueTwice() {
        return invocation -> {
            counter++;
            return counter < 3;
        };
    }

    private Answer<Boolean> answerRandomlyWithAtLeastOneFalse() {
        return invocation -> {
            counter++;
            return counter != 3 && new Random().nextBoolean();
        };
    }

    private Concept randomAction() {
        switch (new Random().nextInt(4)) {
            case 1:
                return new CreateObject(createObjectStimulus());
            case 2:
                return new GetObject(createObjectStimulus());
            case 3:
                return new UpdateObject(createObjectStimulus());
            default:
                return new DeleteObject(createObjectStimulus());
        }
    }

    private Concept randomModifyAction() {
        switch (new Random().nextInt(3)) {
            case 1:
                return new CreateObject(createObjectStimulus());
            case 3:
                return new UpdateObject(createObjectStimulus());
            default:
                return new DeleteObject(createObjectStimulus());
        }
    }

    private QueryResult mockEverything() {
        QueryResult queryResult = mock(QueryResult.class);
        when(queryResult.next()).thenAnswer(answerTrueTwice());
        when(queryResult.getString("name")).thenReturn("expectedObjectName");
        when(queryResult.getString("creator_name")).thenReturn("expectedAgentName");
        when(queryResult.getString("uuid")).thenReturn("adbce68549");
        when(queryResult.getString("value")).thenReturn("value");
        when(queryResult.getString("uuid")).thenReturn("123");
        AID agentAID = mock(AID.class);
        when(agentMock.getAID("expectedAgentName")).thenReturn(agentAID);
        when(dataBaseConnection.execute(anyString())).thenReturn(true);
        when(dataBaseConnection.query(anyString())).thenReturn(queryResult);
        return queryResult;
    }
}
