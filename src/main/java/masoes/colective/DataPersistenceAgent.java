package masoes.colective;


import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;
import ontology.OntologyMatchExpression;
import ontology.masoes.MasoesOntology;
import org.slf4j.LoggerFactory;
import settings.SettingsAgent;
import util.ServiceBuilder;
import util.database.DataBaseHelper;

public class DataPersistenceAgent extends Agent {

    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public DataPersistenceAgent() {
        logger = new AgentLogger(LoggerFactory.getLogger(SettingsAgent.class));
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected void setup() {
        try {
            addBehaviour(new DataPersistenceAgentBehaviour(
                    this,
                    new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())),
                    MasoesOntology.getInstance(),
                    new DataBaseHelper()));

            agentManagementAssistant.register(createService(MasoesOntology.ACTION_CREATE_OBJECT));
            agentManagementAssistant.register(createService(MasoesOntology.ACTION_GET_OBJECT));
            agentManagementAssistant.register(createService(MasoesOntology.ACTION_UPDATE_OBJECT));
            agentManagementAssistant.register(createService(MasoesOntology.ACTION_DELETE_OBJECT));

        } catch (Exception e) {
            logger.exception(this, e);
            throw e;
        }
    }

    private ServiceDescription createService(String serviceName) {
        return new ServiceBuilder()
                .fipaRequest()
                .fipaSL()
                .ontology(MasoesOntology.getInstance())
                .name(serviceName)
                .build();
    }

}
