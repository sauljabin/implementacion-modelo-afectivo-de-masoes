/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import com.google.common.util.concurrent.UncheckedExecutionException;
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
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import ontology.masoes.CreateObject;
import ontology.masoes.DeleteObject;
import ontology.masoes.GetObject;
import ontology.masoes.ListObjects;
import ontology.masoes.MasoesOntology;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
import ontology.masoes.UpdateObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

// TODO: AGREGAR TRANSACCIONES, Y PRUEBAS UNITARIAS DE TRANSACCIONES

public class DataPersistenceBehaviour extends OntologyResponderBehaviour {

    private DataBaseConnection connection;

    public DataPersistenceBehaviour(Agent agent) {
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
        try {
            Concept agentAction = action.getAction();
            if (agentAction instanceof UpdateObject) {
                updateObject((UpdateObject) agentAction);
            } else if (agentAction instanceof CreateObject) {
                createObject((CreateObject) agentAction);
            } else if (agentAction instanceof DeleteObject) {
                deleteObject((DeleteObject) agentAction);
            } else if (agentAction instanceof GetObject) {
                return retrieveObject((GetObject) agentAction);
            }
            return new Done(action);
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }
    }

// TODO: FILTRADO DINAMICO?
// TODO: PORQUE LISTA?
    public ListObjects retrieveObject(GetObject getObjectAction) throws SQLException {

        String sql = String.format("SELECT object.name, object.creator_name, object_property.name, object_property.value " +
                "FROM object INNER JOIN object_property ON object.uuid = object_property.object_uuid " +
                "WHERE object.name='%s';", getObjectAction.getObjectStimulus().getObjectName());

        ListObjects listObjects = new ListObjects();
        listObjects.setObjects(new ArrayList());

        ObjectStimulus objectStimulus = new ObjectStimulus();
        objectStimulus.setObjectProperties(new ArrayList());

        listObjects.getObjects().add(objectStimulus);

        QueryResult resultSet = connection.query(sql);
        while (resultSet.next()) {
            objectStimulus.setObjectName(resultSet.getString(1));
            objectStimulus.setCreator(myAgent.getAID(resultSet.getString(2)));
            ObjectProperty objectProperty = new ObjectProperty(
                    resultSet.getString(3),
                    resultSet.getString(4));
            objectStimulus.getObjectProperties().add(objectProperty);
        }

        return listObjects;
    }

    public void createObject(CreateObject createObjectAction) throws FailureException {
        String newUUID = UUID.randomUUID().toString();
        ObjectStimulus objectStimulus = createObjectAction.getObjectStimulus();
        String creatorName = objectStimulus.getCreator().getLocalName();
        String objectName = objectStimulus.getObjectName();
        String sql = String.format("INSERT INTO object (uuid, name, creator_name) VALUES ('%s', '%s', '%s');", newUUID, objectName, creatorName);
        if (!connection.execute(sql)) {
            throw new FailureException("Operation failed");
        }

        List properties = objectStimulus.getObjectProperties();

        if (properties == null) {
            return;
        }

        for (int i = 0; i < properties.size(); i++) {
            ObjectProperty objectProperty = (ObjectProperty) properties.get(i);
            String name = objectProperty.getName();
            String value = objectProperty.getValue();
            sql = String.format("INSERT INTO object_property (object_uuid, name, value) VALUES ('%s', '%s', '%s');", newUUID, name, value);
            if (!connection.execute(sql)) {
                throw new FailureException("Operation failed");
            }
        }
    }

    public void updateObject(UpdateObject updateObjectAction) throws SQLException, FailureException {
        // TODO: ACTUALIZAR POR NOMBRE Y CREATOR
        String sqlQuery = String.format("SELECT uuid " +
                "FROM object " +
                "WHERE name LIKE '%s' " +
                "AND creator_name LIKE '%s';",
                updateObjectAction.getObjectStimulus().getObjectName(),
                updateObjectAction.getObjectStimulus().getCreator().getName());
        QueryResult resultSet = connection.query(sqlQuery);

        if (resultSet.next()) {
            final String objectUid = resultSet.getString("uuid");
            resultSet.close();
            updateObjectAction.getObjectStimulus().getObjectProperties().iterator().forEachRemaining((Object objectProperty) -> {

                String name = ((ObjectProperty) objectProperty).getName();
                String value = ((ObjectProperty) objectProperty).getValue();

                String sqlUpdateStatement = String.format("UPDATE object_property " +
                        "SET value='%s' " +
                        "WHERE object_uuid LIKE '%s' " +
                        "AND name LIKE '%s';", value, objectUid, name);

                if(!connection.execute(sqlUpdateStatement)) {
                    String sql = String.format("INSERT INTO object_property (name, value, object_uuid) VALUES ('%s', '%s', '%s');", name, value, objectUid);
                    if (!connection.execute(sql)) {
                        throw new UncheckedExecutionException(new FailureException(String.format("Operation failed: Cannot insert data (%s:%s);", name, value)));
                    }
                }
            }
            );
        }
    }

    public void deleteObject(DeleteObject deleteObjectAction) throws SQLException {
        // TODO: ELIMINAR POR NOMBRE Y CREATOR
        String sqlQuery = String.format("SELECT uuid " +
                        "FROM object " +
                        "WHERE name LIKE '%s' " +
                        "AND creator_name LIKE '%s';",
                deleteObjectAction.getObjectStimulus().getObjectName(),
                deleteObjectAction.getObjectStimulus().getCreator().getName());
        QueryResult resultSet = connection.query(sqlQuery);

        if (resultSet.next()) {
            final String objectUid = resultSet.getString("uuid");
            resultSet.close();

            String sqlDeleteProperties = String.format("DELETE FROM object_property WHERE object_uuid LIKE '%s';", objectUid);
            connection.execute(sqlDeleteProperties);
            String sqlDeleteObject = String.format("DELETE FROM object WHERE uuid LIKE '%s';", objectUid);
            connection.execute(sqlDeleteObject);
        }
    }

}
