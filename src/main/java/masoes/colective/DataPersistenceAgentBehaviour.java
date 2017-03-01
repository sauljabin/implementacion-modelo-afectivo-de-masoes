package masoes.colective;

import jade.content.onto.Ontology;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import ontology.OntologyResponderBehaviour;
import ontology.masoes.CreateObject;
import ontology.masoes.DeleteObject;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetObject;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
import ontology.masoes.Stimulus;
import ontology.masoes.UpdateObject;
import util.database.DataBaseException;
import util.database.DataBaseHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataPersistenceAgentBehaviour extends OntologyResponderBehaviour {

    private static String SELECT_CLAUSE = "SELECT Creator.object_name, Properties.key, Properties.value " +
                                                "FROM Creator INNER JOIN Properties ON Creator.id = Properties.creator_id " +
                                                "WHERE Creator.object_name like \"%s\"" ;

    private DataBaseHelper dataBaseHelper;

    public DataPersistenceAgentBehaviour(Agent agent, MessageTemplate messageTemplate, Ontology ontology, DataBaseHelper dataBaseHelper) {
        super(agent, messageTemplate, ontology);
        this.dataBaseHelper = dataBaseHelper;
    }

    public EvaluateStimulus retrieveObject(GetObject getObjectAction) throws FailureException {
        try {
            dataBaseHelper.openConnection();
            String retrieveClause = String.format(SELECT_CLAUSE, getObjectAction.getObjectStimulus().getObjectName());
            ResultSet resultSet = dataBaseHelper.performRetrieveStatement(retrieveClause);
            EvaluateStimulus evaluateStimulus = new EvaluateStimulus(castResult(resultSet));
            dataBaseHelper.closeConnection();
            return evaluateStimulus;
        } catch (DataBaseException e) {
            handleDataBaseExceptionAndThrowFailureException(e);
            return null;
        }
    }

    private Stimulus castResult(ResultSet resultSet) throws DataBaseException {
        try {
            ObjectStimulus objectStimulus = new ObjectStimulus();
            List propertiesList = new ArrayList();
            while (resultSet.next()) {
                objectStimulus.setObjectName(resultSet.getString("Creator.object_name"));
                propertiesList.add(new ObjectProperty(resultSet.getString("Property.name"), resultSet.getString("Property.value")));
            }
            objectStimulus.setObjectProperties(propertiesList);
            return objectStimulus;
        } catch (SQLException e){
            throw new DataBaseException(e.getMessage(), true);
        }
    }

    public Done updateObject(UpdateObject updateObjectAction) {
        return new Done();
    }

    public Done createObject(CreateObject createObjectAction) {
        return new Done();
    }

    public Done deleteObject(DeleteObject deleteObjectAction) {
        return new Done();
    }

    private void handleDataBaseExceptionAndThrowFailureException(DataBaseException e) throws FailureException {
        String exceptionMessage = e.getMessage();
        if(e.isCloseConnectionNeeded()){
            try {
                dataBaseHelper.closeConnection();
            } catch (Exception ex){
                exceptionMessage = String.format("DataBase execution error: ½s, DataBase closing error: ½s", exceptionMessage, ex.getMessage());
            }
        }
        e.printStackTrace();
        throw new FailureException(exceptionMessage);
    }
}
