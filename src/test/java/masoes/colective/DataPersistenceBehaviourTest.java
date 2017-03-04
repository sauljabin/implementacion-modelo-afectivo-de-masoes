/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import com.google.common.util.concurrent.UncheckedExecutionException;
import data.DataBaseConnection;
import data.QueryResult;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
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
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import test.PowerMockitoTest;
import test.ReflectionTestUtils;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

//Should be component test
public class DataPersistenceBehaviourTest extends PowerMockitoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private DataPersistenceBehaviour dataPersistenceBehaviour;
    private Agent agentMock;
    private DataBaseConnection dataBaseConnection;

    @Before
    public void setUp() {
        agentMock = mock(Agent.class);
        dataPersistenceBehaviour = new DataPersistenceBehaviour(agentMock);
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

    private ObjectStimulus createObjectStimulus() {
        String expectedObjectName = "expectedObjectName";
        String expectedAgentName = "expectedAgentName";

        String expectedPropertyValue = "expectedPropertyValue";
        String expectedPropertyName = "expectedPropertyName";

        AID creator = new AID(expectedAgentName, AID.ISGUID);

        ArrayList objectProperties = new ArrayList();
        objectProperties.add(new ObjectProperty(expectedPropertyName, expectedPropertyValue));

        ObjectStimulus objectStimulus = new ObjectStimulus();
        objectStimulus.setObjectName(expectedObjectName);
        objectStimulus.setCreator(creator);
        objectStimulus.setObjectProperties(objectProperties);

        return objectStimulus;
    }

    @Test
    public void shouldRetrieveObjectListGivenAnObjectAction() throws Exception {
        QueryResult resultSetValue = mock(QueryResult.class);
        when(resultSetValue.next()).thenAnswer(new Answer<Boolean>() {
            private boolean auxiliarValue = false;

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                auxiliarValue = !auxiliarValue;
                return auxiliarValue;
            }
        });
        when(resultSetValue.getString(1)).thenReturn("expectedObjectName");
        when(resultSetValue.getString(2)).thenReturn("expectedObjectCreatorName");
        when(resultSetValue.getString(3)).thenReturn("alias");
        when(resultSetValue.getString(4)).thenReturn("eltatuado");

        DataBaseConnection dataBaseConnection = mock(DataBaseConnection.class);
        when(dataBaseConnection.query(anyString())).thenReturn(resultSetValue);
        ReflectionTestUtils.setFieldValue(dataPersistenceBehaviour, "connection", dataBaseConnection);

        GetObject getObjectAction = new GetObject(createObjectStimulus());
        ListObjects actualListObjects = dataPersistenceBehaviour.retrieveObject(getObjectAction);

        ObjectStimulus actual = (ObjectStimulus) actualListObjects.getObjects().get(0);
        assertThat(actual.getObjectName(), is("expectedObjectName"));
        ObjectProperty actualProperty = (ObjectProperty) actual.getObjectProperties().get(0);
        assertThat(actualProperty.getName(), is("alias"));
        assertThat(actualProperty.getValue(), is("eltatuado"));
    }

    @Test
    public void shouldUpdatePropertyValues() throws Exception {
        UpdateObject updateObject = new UpdateObject(createObjectStimulus());
        DataBaseConnection dataBaseConnection = mock(DataBaseConnection.class);
        ReflectionTestUtils.setFieldValue(dataPersistenceBehaviour, "connection", dataBaseConnection);
        QueryResult resultSetValue = mock(QueryResult.class);
        when(resultSetValue.next()).thenReturn(true);
        when(resultSetValue.getString("uuid")).thenReturn("123456");
        when(dataBaseConnection.query(anyString())).thenReturn(resultSetValue);
        when(dataBaseConnection.execute(anyString())).thenReturn(true);

        dataPersistenceBehaviour.updateObject(updateObject);


        String expectedSqlQuery = "SELECT uuid FROM object WHERE name LIKE 'expectedObjectName' AND creator_name LIKE 'expectedAgentName';";
        String expectedUpdateQuery = "UPDATE object_property SET value='expectedPropertyValue' WHERE object_uuid LIKE '123456' " +
                "AND name LIKE 'expectedPropertyName';";


        Mockito.verify(dataBaseConnection).query(expectedSqlQuery);
        Mockito.verify(dataBaseConnection).execute(expectedUpdateQuery);

    }

    @Test
    public void shouldAddNewPropertiesWhenUpdatesDoesNothing() throws Exception {
        UpdateObject updateObject = new UpdateObject(createObjectStimulus());
        DataBaseConnection dataBaseConnection = mock(DataBaseConnection.class);
        ReflectionTestUtils.setFieldValue(dataPersistenceBehaviour, "connection", dataBaseConnection);
        QueryResult resultSetValue = mock(QueryResult.class);
        when(resultSetValue.next()).thenReturn(true);
        when(resultSetValue.getString("uuid")).thenReturn("123456");
        when(dataBaseConnection.query(anyString())).thenReturn(resultSetValue);
        when(dataBaseConnection.execute(anyString())).thenAnswer(new Answer<Boolean>() {
            private boolean auxiliarResponse = true;

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                auxiliarResponse = !auxiliarResponse;
                return auxiliarResponse;
            }
        });

        dataPersistenceBehaviour.updateObject(updateObject);


        String expectedSqlQuery = "SELECT uuid FROM object WHERE name LIKE 'expectedObjectName' AND creator_name LIKE 'expectedAgentName';";
        String expectedUpdateQuery = "UPDATE object_property SET value='expectedPropertyValue' WHERE object_uuid LIKE '123456' " +
                "AND name LIKE 'expectedPropertyName';";
        String expectedInsertStatement = "INSERT INTO object_property (name, value, object_uuid) VALUES ('expectedPropertyName', 'expectedPropertyValue', '123456');";


        Mockito.verify(dataBaseConnection).query(expectedSqlQuery);
        Mockito.verify(dataBaseConnection).execute(expectedUpdateQuery);
        Mockito.verify(dataBaseConnection).execute(expectedInsertStatement);

    }

    @Test
    public void shouldThrowExceptionIfErrorOccursWhenInsertingProperties() throws Exception {
        expectedException.expect(UncheckedExecutionException.class);
        expectedException.expectMessage("Operation failed: Cannot insert data (expectedPropertyName:expectedPropertyValue)");

        UpdateObject updateObject = new UpdateObject(createObjectStimulus());
        DataBaseConnection dataBaseConnection = mock(DataBaseConnection.class);
        ReflectionTestUtils.setFieldValue(dataPersistenceBehaviour, "connection", dataBaseConnection);
        QueryResult resultSetValue = mock(QueryResult.class);
        when(resultSetValue.next()).thenReturn(true);
        when(resultSetValue.getString("uuid")).thenReturn("123456");
        when(dataBaseConnection.query(anyString())).thenReturn(resultSetValue);
        when(dataBaseConnection.execute(anyString())).thenReturn(false);


        dataPersistenceBehaviour.updateObject(updateObject);


    }

    @Test
    public void shouldDeleteObjectWithProperties() throws Exception {
        DeleteObject deleteObject = new DeleteObject(createObjectStimulus());
        DataBaseConnection dataBaseConnection = mock(DataBaseConnection.class);
        ReflectionTestUtils.setFieldValue(dataPersistenceBehaviour, "connection", dataBaseConnection);
        QueryResult resultSetValue = mock(QueryResult.class);
        when(resultSetValue.next()).thenReturn(true);
        when(resultSetValue.getString("uuid")).thenReturn("123456");
        when(dataBaseConnection.query(anyString())).thenReturn(resultSetValue);
        when(dataBaseConnection.execute(anyString())).thenReturn(false);


        String expectedSqlQuery = "SELECT uuid FROM object WHERE name LIKE 'expectedObjectName' AND creator_name LIKE 'expectedAgentName';";
        String expectedDeleteStatementForProperties = "DELETE FROM object_property WHERE object_uuid LIKE '123456';";
        String expectedDeleteStatementForObject = "DELETE FROM object WHERE uuid LIKE '123456';";


        dataPersistenceBehaviour.deleteObject(deleteObject);

        Mockito.verify(dataBaseConnection).query(expectedSqlQuery);
        Mockito.verify(dataBaseConnection).execute(expectedDeleteStatementForProperties);
        Mockito.verify(dataBaseConnection).execute(expectedDeleteStatementForObject);

    }


}