/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import data.DataBaseConnection;
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

import java.util.Arrays;
import java.util.UUID;

// TODO: AGREGAR TRANSACCIONES, Y PRUEBAS UNITARIAS DE TRANSACCIONES
// TODO: SE PUEDE CREAR DB DESDE PRUEBA?, PARA NO CORRER MIGRATE SIEMPRE

public class DataPersistenceBehaviour extends OntologyResponderBehaviour {

    private DataBaseConnection connection;

    public DataPersistenceBehaviour(Agent agent) {
        super(agent, new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), MasoesOntology.getInstance());
    }

    @Override
    public void onStart() {
        connection = DataBaseConnection.getConnection();
    }

    @Override
    public int onEnd() {
        connection.closeConnection();
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
    }

    public ListObjects retrieveObject(GetObject getObjectAction) throws FailureException {
        try {
            /*
            ObjectStimulus objectQuery = getObjectAction.getObjectStimulus();
            String sql = String.format("SELECT object.name, object.creator_name, object_property.name, object_property.value " +
                    "FROM object INNER JOIN object_property ON object.uuid = object_property.object_uuid " +
                    "WHERE object.name='%s'", objectQuery.getObjectName());

            // TODO: FILTRADO DINAMICO
            // TODO: DEVOLVER LISTO DE OBJETOS
            // TODO: PROBAR

             ListObjects listObjects = new ListObjects();
            listObjects.setObjects(new ArrayList());

            ObjectStimulus objectStimulus = new ObjectStimulus();
            objectStimulus.setObjectProperties(new ArrayList());

            listObjects.getObjects().add(objectStimulus);

            ResultSet resultSet = connection.query("select");
            while (resultSet.next()) {
                String objectName = resultSet.getString("object.name");
                AID creator = myAgent.getAID(resultSet.getString("object.creator_name"));
                String propertyName = resultSet.getString("object_property.name");
                String propertyValue = resultSet.getString("object_property.value");

                objectStimulus.setObjectName(objectName);
                objectStimulus.setCreator(creator);
                objectStimulus.getObjectProperties().add(new ObjectProperty(propertyName, propertyValue));
            }
            */

            ArrayList objects = new ArrayList();
            objects.add(new ObjectStimulus());
            return new ListObjects(objects);
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }
    }

    public void createObject(CreateObject createObjectAction) throws FailureException {
        String newUUID = UUID.randomUUID().toString();
        ObjectStimulus objectStimulus = createObjectAction.getObjectStimulus();
        String creatorName = objectStimulus.getCreator().getLocalName();
        String objectName = objectStimulus.getObjectName();
        String sql = String.format("INSERT INTO object (uuid, name, creator_name) VALUES ('%s', '%s', '%s')", newUUID, objectName, creatorName);
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
            sql = String.format("INSERT INTO object_property (object_uuid, name, value) VALUES ('%s', '%s', '%s')", newUUID, name, value);
            if (!connection.execute(sql)) {
                throw new FailureException("Operation failed");
            }
        }
    }

    public void updateObject(UpdateObject updateObjectAction) {
        // TODO: ACTUALIZAR POR NOMBRE Y CREATOR
    }

    public void deleteObject(DeleteObject deleteObjectAction) {
        // TODO: ELIMINAR POR NOMBRE Y CREATOR
    }

}