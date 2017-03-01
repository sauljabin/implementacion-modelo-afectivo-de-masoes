package masoes.colective;

import data.DataBaseConnection;
import data.DataBaseException;
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
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetObject;
import ontology.masoes.MasoesOntology;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
import ontology.masoes.Stimulus;
import ontology.masoes.UpdateObject;

import java.sql.ResultSet;
import java.util.Arrays;

public class DataPersistenceBehaviour extends OntologyResponderBehaviour {

    private DataBaseConnection connection;

    public DataPersistenceBehaviour(Agent agent) {
        super(agent, new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), MasoesOntology.getInstance());
        this.connection = DataBaseConnection.getConnection();
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
        }
        return new Done(action);
    }

    public EvaluateStimulus retrieveObject(GetObject getObjectAction) throws FailureException {
        try {
            String selectClause = "SELECT object.name, object_property.name, object_property.value " +
                    "FROM object INNER JOIN object_property ON object.uuid = object_property.object_uuid " +
                    "WHERE object.name like '%s'";
            String retrieveClause = String.format(selectClause, getObjectAction.getObjectStimulus().getObjectName());
            ResultSet query = connection.query(retrieveClause);
            EvaluateStimulus evaluateStimulus = new EvaluateStimulus(castResult(query));
            return evaluateStimulus;
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }
    }

    private Stimulus castResult(ResultSet resultSet) {
        try {
            ObjectStimulus objectStimulus = new ObjectStimulus();
            List propertiesList = new ArrayList();
            while (resultSet.next()) {
                objectStimulus.setObjectName(resultSet.getString("object.name"));
                propertiesList.add(new ObjectProperty(resultSet.getString("object_property.name"), resultSet.getString("object_property.value")));
            }
            objectStimulus.setObjectProperties(propertiesList);
            return objectStimulus;
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public void updateObject(UpdateObject updateObjectAction) {
    }

    public void createObject(CreateObject createObjectAction) {
    }

    public void deleteObject(DeleteObject deleteObjectAction) {
    }

}
