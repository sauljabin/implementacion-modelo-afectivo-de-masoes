/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import data.DataBaseConnection;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import ontology.OntologyMatchExpression;
import ontology.masoes.CreateObject;
import ontology.masoes.DeleteObject;
import ontology.masoes.GetObject;
import ontology.masoes.MasoesOntology;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
import ontology.masoes.UpdateObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.PhoenixDatabase;
import test.PowerMockitoTest;

import java.sql.ResultSet;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DataPersistenceBehaviourTest extends PowerMockitoTest {

    private DataPersistenceBehaviour dataPersistenceBehaviour;
    private Agent agentMock;
    private DataBaseConnection dataBaseConnection;

    @Before
    public void setUp() {
        dataBaseConnection = PhoenixDatabase.create();
        agentMock = mock(Agent.class);
        dataPersistenceBehaviour = new DataPersistenceBehaviour(agentMock);
        dataPersistenceBehaviour.onStart();
    }

    @After
    public void tearDown() {
        PhoenixDatabase.destroy();
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
    public void shouldCreateObject() throws Exception {
        ObjectStimulus objectStimulus = createObjectStimulus();

        CreateObject createObject = new CreateObject();
        createObject.setObjectStimulus(objectStimulus);

        Action action = new Action();
        action.setAction(createObject);

        Predicate predicate = dataPersistenceBehaviour.performAction(action);
        assertThat(predicate, is(instanceOf(Done.class)));

        ResultSet query = dataBaseConnection.query("select * from object");
        assertTrue(query.next());
        assertThat(query.getString("name"), is(objectStimulus.getObjectName()));
        assertThat(query.getString("creator_name"), is(objectStimulus.getCreator().getLocalName()));

        String uuid = query.getString("uuid");
        ObjectProperty objectProperty = (ObjectProperty) objectStimulus.getObjectProperties().get(0);

        query = dataBaseConnection.query("select * from object_property");
        assertTrue(query.next());
        assertThat(query.getString("name"), is(objectProperty.getName()));
        assertThat(query.getString("value"), is(objectProperty.getValue()));
        assertThat(query.getString("object_uuid"), is(uuid));
    }

    private ObjectStimulus createObjectStimulus() {
        String expectedObjectName = "expectedObjectName";
        String expectedAgentName = "expectedAgentName";

        String expectedPropertyValue = "expectedPropertyValue";
        String expectedPropertyName = "expectedPropertyName";

        ArrayList objectProperties = new ArrayList();
        objectProperties.add(new ObjectProperty(expectedPropertyName, expectedPropertyValue));

        ObjectStimulus objectStimulus = new ObjectStimulus();
        objectStimulus.setObjectName(expectedObjectName);
        objectStimulus.setCreator(new AID(expectedAgentName, AID.ISGUID));
        objectStimulus.setObjectProperties(objectProperties);
        return objectStimulus;
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(dataPersistenceBehaviour.getOntology(), is(instanceOf(MasoesOntology.class)));
        assertReflectionEquals(new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), dataPersistenceBehaviour.getMessageTemplate());
    }

}