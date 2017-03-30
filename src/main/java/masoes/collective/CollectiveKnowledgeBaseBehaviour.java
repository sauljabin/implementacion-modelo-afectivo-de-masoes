/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import data.DataBaseConnection;
import data.QueryResult;
import jade.content.Concept;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import masoes.ontology.MasoesOntology;
import masoes.ontology.knowledge.CreateObject;
import masoes.ontology.knowledge.DeleteObject;
import masoes.ontology.knowledge.GetObject;
import masoes.ontology.knowledge.ListObjects;
import masoes.ontology.knowledge.ObjectEnvironment;
import masoes.ontology.knowledge.ObjectProperty;
import masoes.ontology.knowledge.UpdateObject;
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;

import java.util.Arrays;
import java.util.UUID;

public class CollectiveKnowledgeBaseBehaviour extends OntologyResponderBehaviour {

    private DataBaseConnection connection;

    public CollectiveKnowledgeBaseBehaviour(Agent agent) {
        super(agent, new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), MasoesOntology.getInstance());
        connection = DataBaseConnection.getConnection();
    }

    @Override
    public void onStart() {
        connection.connect();
    }

    @Override
    public int onEnd() {
        connection.close();
        return 0;
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetObject.class, CreateObject.class, UpdateObject.class, DeleteObject.class)
                .contains(action.getAction().getClass());
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        Concept agentAction = action.getAction();
        try {
            if (agentAction instanceof GetObject) {
                return retrieveObject((GetObject) agentAction);
            }

            connection.beginTransaction();
            if (agentAction instanceof UpdateObject) {
                updateObject((UpdateObject) agentAction);
            } else if (agentAction instanceof CreateObject) {
                createObject((CreateObject) agentAction);
            } else if (agentAction instanceof DeleteObject) {
                deleteCompleteObject((DeleteObject) agentAction);
            }
            connection.endTransaction();
            return new Done(action);
        } catch (FailureException e) {
            if (!(agentAction instanceof GetObject)) {
                connection.rollbackTransaction();
            }
            throw e;
        }
    }

    private void createObject(CreateObject createObjectAction) throws FailureException {
        String newUUID = UUID.randomUUID().toString();
        String creatorName = createObjectAction.getObjectEnvironment().getCreator().getLocalName();
        String objectName = createObjectAction.getObjectEnvironment().getName();

        insertObject(newUUID, creatorName, objectName);

        List properties = createObjectAction.getObjectEnvironment().getObjectProperties();

        if (properties != null) {
            insertObjectProperties(newUUID, properties);
        }

    }

    private ListObjects retrieveObject(GetObject getObjectAction) throws FailureException {
        ObjectEnvironment objectEnvironment = retrieveSingleObject(getObjectAction.getObjectEnvironment().getName(), getObjectAction.getObjectEnvironment().getCreator().getLocalName());
        ListObjects listObjects = new ListObjects();
        listObjects.setObjects(new ArrayList());
        listObjects.getObjects().add(objectEnvironment);
        return listObjects;
    }

    private void updateObject(UpdateObject updateObjectAction) throws FailureException {
        String uuid = getObjectUniqueIdentification(updateObjectAction.getObjectEnvironment().getName(), updateObjectAction.getObjectEnvironment().getCreator().getLocalName());
        deleteProperties(uuid);
        if (updateObjectAction.getObjectEnvironment().getObjectProperties() != null) {
            insertObjectProperties(uuid, updateObjectAction.getObjectEnvironment().getObjectProperties());
        }
    }

    private void deleteCompleteObject(DeleteObject deleteObjectAction) throws FailureException {
        String objectUuid = getObjectUniqueIdentification(deleteObjectAction.getObjectEnvironment().getName(), deleteObjectAction.getObjectEnvironment().getCreator().getLocalName());
        deleteProperties(objectUuid);
        deleteObjectOnly(objectUuid);
    }

    private void insertObject(String newUUID, String creatorName, String objectName) throws FailureException {
        String sql = String.format("INSERT INTO object (uuid, name, creator_name) VALUES ('%s', '%s', '%s');", newUUID, objectName, creatorName);
        if (!connection.execute(sql)) {
            throw new FailureException("Operation failed");
        }
    }

    private void insertObjectProperties(String objectUuid, List properties) throws FailureException {
        for (int i = 0; i < properties.size(); i++) {
            ObjectProperty objectProperty = (ObjectProperty) properties.get(i);
            String name = objectProperty.getName();
            String value = objectProperty.getValue();
            String sql = String.format("INSERT INTO object_property (object_uuid, name, value) VALUES ('%s', '%s', '%s');", objectUuid, name, value);
            if (!connection.execute(sql)) {
                throw new FailureException("Operation failed");
            }
        }
    }

    private ObjectEnvironment retrieveSingleObject(String objectName, String creatorName) throws FailureException {
        String sqlQueryObject = String.format("SELECT name, creator_name, uuid FROM object " +
                "WHERE object.name LIKE '%s' AND object.creator_name LIKE '%s';", objectName, creatorName);
        QueryResult queryResult = connection.query(sqlQueryObject);
        try {
            if (queryResult.next()) {
                return new ObjectEnvironment(myAgent.getAID(queryResult.getString("creator_name")),
                        queryResult.getString("name"),
                        getObjectPropertiesList(queryResult.getString("uuid")));
            } else {
                throw new FailureException("No such object: " + objectName);
            }
        } finally {
            queryResult.close();
        }
    }

    private List getObjectPropertiesList(String objectUuid) {
        List propertyList = new ArrayList();
        String sql = String.format("SELECT name, value FROM object_property WHERE object_uuid LIKE '%s';", objectUuid);
        QueryResult resultSet = connection.query(sql);
        while (resultSet.next()) {
            ObjectProperty objectProperty = new ObjectProperty(resultSet.getString("name"), resultSet.getString("value"));
            propertyList.add(objectProperty);
        }
        resultSet.close();
        return propertyList;
    }

    private String getObjectUniqueIdentification(String name, String creatorName) throws FailureException {
        String sqlQuery = String.format("SELECT uuid FROM object WHERE name LIKE '%s' AND creator_name LIKE '%s';", name, creatorName);
        QueryResult queryResult = connection.query(sqlQuery);
        try {
            if (queryResult.next()) {
                return queryResult.getString("uuid");
            } else {
                throw new FailureException("No such object: " + name);
            }
        } finally {
            queryResult.close();
        }
    }

    private void deleteProperties(String objectUuid) {
        String sqlDeleteProperties = String.format("DELETE FROM object_property WHERE object_uuid LIKE '%s';", objectUuid);
        connection.execute(sqlDeleteProperties);
    }

    private void deleteObjectOnly(String objectUuid) {
        String sqlDeleteObject = String.format("DELETE FROM object WHERE uuid LIKE '%s';", objectUuid);
        connection.execute(sqlDeleteObject);
    }

}
