/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import data.DataBaseConnection;
import data.QueryResult;
import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import masoes.ontology.MasoesOntology;
import masoes.ontology.knowledge.CreateObject;
import masoes.ontology.knowledge.DeleteObject;
import masoes.ontology.knowledge.GetObject;
import masoes.ontology.knowledge.ListObjects;
import masoes.ontology.knowledge.ObjectEnvironment;
import masoes.ontology.knowledge.ObjectProperty;
import masoes.ontology.knowledge.UpdateObject;
import ontology.OntologyAssistant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.ProtocolAssistant;
import test.FunctionalTest;
import test.PhoenixDatabase;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ColectiveKnowledgeBaseAgentFunctionalTest extends FunctionalTest {

    private AID persistenceAgent;
    private OntologyAssistant ontologyAssistant;
    private ProtocolAssistant protocolAssistant;
    private DataBaseConnection connection;

    @Before
    public void setUp() {
        connection = PhoenixDatabase.create();
        persistenceAgent = createAgent(ColectiveKnowledgeBaseAgent.class, null);
        ontologyAssistant = createOntologyAssistant(MasoesOntology.getInstance());
        protocolAssistant = createProtocolAssistant();
    }

    @After
    public void tearDown() {
        killAgent(persistenceAgent);
        PhoenixDatabase.destroy();
    }

    @Test
    public void shouldGetAllServicesFromDF() {
        List<ServiceDescription> services = services(persistenceAgent);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(
                MasoesOntology.ACTION_GET_OBJECT,
                MasoesOntology.ACTION_CREATE_OBJECT,
                MasoesOntology.ACTION_DELETE_OBJECT,
                MasoesOntology.ACTION_UPDATE_OBJECT
        ));
    }

    @Test
    public void shouldCreateObject() {
        String expectedObjectName = "expectedObjectName";
        String expectedPropertyName = "expectedPropertyName";
        String expectedPropertyValue = "expectedPropertyValue";
        String expectedCreatorName = "expectedCreatorName";
        ArrayList objectProperties = new ArrayList();
        objectProperties.add(new ObjectProperty(expectedPropertyName, expectedPropertyValue));
        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setName(expectedObjectName);
        objectEnvironment.setCreator(new AID(expectedCreatorName, AID.ISGUID));
        objectEnvironment.setObjectProperties(objectProperties);

        CreateObject createObject = new CreateObject();
        createObject.setObjectEnvironment(objectEnvironment);

        ContentElement contentElement = sendAction(createObject);
        assertThat(contentElement, is(instanceOf(Done.class)));

        QueryResult queryResult = connection.query("select * from object");
        assertTrue(queryResult.next());
        assertThat(queryResult.getString("name"), is(expectedObjectName));
        assertThat(queryResult.getString("creator_name"), is(expectedCreatorName));
        String uuid = queryResult.getString("uuid");
        assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));

        QueryResult queryResultProperty = connection.query("select * from object_property");
        assertTrue(queryResultProperty.next());
        assertThat(queryResultProperty.getString("object_uuid"), is(uuid));
        assertThat(queryResultProperty.getString("name"), is(expectedPropertyName));
        assertThat(queryResultProperty.getString("value"), is(expectedPropertyValue));
    }

    @Test
    public void shouldGetObject() {
        String uuid = "uuidToDelete";
        String expectedObjectName = "expectedObjectName";
        String expectedCreatorName = "expectedCreatorName";
        String expectedPropertyName = "expectedPropertyName";
        String expectedPropertyValue = "expectedPropertyValue";

        connection.execute(String.format("insert into object (uuid, name, creator_name) values ('%s', '%s', '%s')", uuid, expectedObjectName, expectedCreatorName));
        connection.execute(String.format("insert into object_property (object_uuid, name, value) values ('%s', '%s', '%s')", uuid, expectedPropertyName, expectedPropertyValue));

        GetObject getObject = new GetObject();
        ObjectEnvironment getObjectEnvironment = new ObjectEnvironment();
        getObjectEnvironment.setCreator(new AID(expectedCreatorName, AID.ISGUID));
        getObjectEnvironment.setName(expectedObjectName);
        getObject.setObjectEnvironment(getObjectEnvironment);

        ContentElement contentElementGetObject = sendAction(getObject);

        assertThat(contentElementGetObject, is(instanceOf(ListObjects.class)));
        ListObjects listObjects = (ListObjects) contentElementGetObject;

        jade.util.leap.List objects = listObjects.getObjects();
        assertThat(objects.size(), is(1));

        ObjectEnvironment actualObject = (ObjectEnvironment) objects.get(0);
        assertThat(actualObject.getName(), is(expectedObjectName));
        assertThat(actualObject.getCreator().getLocalName(), is(expectedCreatorName));

        jade.util.leap.List objectProperties = actualObject.getObjectProperties();
        assertThat(objectProperties.size(), is(1));

        ObjectProperty objectProperty = (ObjectProperty) objectProperties.get(0);
        assertThat(objectProperty.getName(), is(expectedPropertyName));
        assertThat(objectProperty.getValue(), is(expectedPropertyValue));
    }

    @Test
    public void shouldDeleteObject() {
        String uuid = "uuidToDelete";
        String nameToDelete = "nameToDelete";
        String creatorNameToDelete = "creatorNameToDelete";
        connection.execute(String.format("insert into object (uuid, name, creator_name) values ('%s', '%s', '%s')", uuid, nameToDelete, creatorNameToDelete));
        connection.execute(String.format("insert into object_property (object_uuid, name, value) values ('%s', 'any', 'any')", uuid));

        DeleteObject deleteObject = new DeleteObject();
        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setCreator(new AID(creatorNameToDelete, AID.ISGUID));
        objectEnvironment.setName(nameToDelete);
        deleteObject.setObjectEnvironment(objectEnvironment);
        ContentElement contentElement = sendAction(deleteObject);
        assertThat(contentElement, is(instanceOf(Done.class)));

        QueryResult queryResult = connection.query(String.format("select * from object where uuid = '%s'", uuid));
        assertFalse(queryResult.next());

        QueryResult queryResultProperty = connection.query(String.format("select * from object_property where object_uuid = '%s'", uuid));
        assertFalse(queryResultProperty.next());
    }

    @Test
    public void shouldUpdateObject() {
        String uuid = "uuid";
        String objectName = "objectName";
        String creatorName = "creatorName";
        String propertyName = "propertyName";
        String propertyValue = "propertyValue";
        connection.execute(String.format("insert into object (uuid, name, creator_name) values ('%s', '%s', '%s')", uuid, objectName, creatorName));
        connection.execute(String.format("insert into object_property (object_uuid, name, value) values ('%s', '%s', '%s')", uuid, propertyName, propertyValue));

        String expectedValue = "expectedValue";

        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setName(objectName);
        objectEnvironment.setCreator(new AID(creatorName, AID.ISGUID));
        objectEnvironment.setObjectProperties(new ArrayList());
        objectEnvironment.getObjectProperties().add(new ObjectProperty(propertyName, expectedValue));

        UpdateObject updateObject = new UpdateObject();
        updateObject.setObjectEnvironment(objectEnvironment);

        ContentElement contentElement = sendAction(updateObject);
        assertThat(contentElement, is(instanceOf(Done.class)));

        QueryResult queryResult = connection.query("select * from object");
        assertTrue(queryResult.next());
        assertThat(queryResult.getString("name"), is(objectName));
        assertThat(queryResult.getString("creator_name"), is(creatorName));

        QueryResult queryResultProperty = connection.query("select * from object_property");
        assertTrue(queryResultProperty.next());
        assertThat(queryResultProperty.getString("name"), is(propertyName));
        assertThat(queryResultProperty.getString("value"), is(expectedValue));
    }

    @Test
    public void shouldUpdateAndAddObject() {
        String uuid = "uuid";
        String objectName = "objectName";
        String creatorName = "creatorName";
        String propertyName = "propertyName";
        String propertyValue = "propertyValue";
        connection.execute(String.format("insert into object (uuid, name, creator_name) values ('%s', '%s', '%s')", uuid, objectName, creatorName));
        connection.execute(String.format("insert into object_property (object_uuid, name, value) values ('%s', '%s', '%s')", uuid, propertyName, propertyValue));

        String secondPropertyValue = "secondPropertyValue";
        String secondPropertyName = "secondPropertyName";
        String expectedFirstPropertyValue = "expectedFirstPropertyValue";

        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setName(objectName);
        objectEnvironment.setCreator(new AID(creatorName, AID.ISGUID));
        objectEnvironment.setObjectProperties(new ArrayList());
        objectEnvironment.getObjectProperties().add(new ObjectProperty(propertyName, expectedFirstPropertyValue));
        objectEnvironment.getObjectProperties().add(new ObjectProperty(secondPropertyName, secondPropertyValue));

        UpdateObject updateObject = new UpdateObject();
        updateObject.setObjectEnvironment(objectEnvironment);

        ContentElement contentElement = sendAction(updateObject);
        assertThat(contentElement, is(instanceOf(Done.class)));

        QueryResult queryResultProperty = connection.query(String.format("select * from object_property where name = '%s'", propertyName));
        assertTrue(queryResultProperty.next());
        assertThat(queryResultProperty.getString("name"), is(propertyName));
        assertThat(queryResultProperty.getString("value"), is(expectedFirstPropertyValue));

        QueryResult queryResultSecondProperty = connection.query(String.format("select * from object_property where name = '%s'", secondPropertyName));
        assertTrue(queryResultSecondProperty.next());
        assertThat(queryResultSecondProperty.getString("name"), is(secondPropertyName));
        assertThat(queryResultSecondProperty.getString("value"), is(secondPropertyValue));
    }

    @Test
    public void shouldUpdateAndRemoveObject() {
        String uuid = "uuid";
        String objectName = "objectName";
        String creatorName = "creatorName";
        String propertyName = "propertyName";
        String propertyValue = "propertyValue";
        String secondPropertyName = "secondPropertyName";
        String secondPropertyValue = "secondPropertyValue";
        connection.execute(String.format("insert into object (uuid, name, creator_name) values ('%s', '%s', '%s')", uuid, objectName, creatorName));
        connection.execute(String.format("insert into object_property (object_uuid, name, value) values ('%s', '%s', '%s')", uuid, propertyName, propertyValue));
        connection.execute(String.format("insert into object_property (object_uuid, name, value) values ('%s', '%s', '%s')", uuid, secondPropertyName, secondPropertyValue));

        String expectedValue = "expectedValue";

        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setName(objectName);
        objectEnvironment.setCreator(new AID(creatorName, AID.ISGUID));
        objectEnvironment.setObjectProperties(new ArrayList());
        objectEnvironment.getObjectProperties().add(new ObjectProperty(propertyName, expectedValue));

        UpdateObject updateObject = new UpdateObject();
        updateObject.setObjectEnvironment(objectEnvironment);

        ContentElement contentElement = sendAction(updateObject);
        assertThat(contentElement, is(instanceOf(Done.class)));

        QueryResult queryResultProperty = connection.query(String.format("select * from object_property where name = '%s'", propertyName));
        assertTrue(queryResultProperty.next());
        assertThat(queryResultProperty.getString("name"), is(propertyName));
        assertThat(queryResultProperty.getString("value"), is(expectedValue));

        QueryResult queryResultSecondProperty = connection.query(String.format("select * from object_property where name = '%s'", secondPropertyName));
        assertFalse(queryResultSecondProperty.next());
    }

    private ContentElement sendAction(AgentAction action) {
        ACLMessage requestAction = ontologyAssistant.createRequestAction(persistenceAgent, action);
        ACLMessage message = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);
        return ontologyAssistant.extractMessageContent(message);
    }

}