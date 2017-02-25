/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import masoes.behavioural.BehaviouralComponent;
import ontology.masoes.MasoesOntology;
import org.slf4j.LoggerFactory;
import settings.SettingsAgent;
import util.ServiceBuilder;
import util.ToStringBuilder;

public abstract class EmotionalAgent extends Agent {

    private BehaviouralComponent behaviouralComponent;
    private AgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public EmotionalAgent() {
        logger = new AgentLogger(LoggerFactory.getLogger(SettingsAgent.class));
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected final void setup() {
        try {
            setUp();
            behaviouralComponent = new BehaviouralComponent(this);
            addBehaviour(new BasicEmotionalAgentBehaviour(this));
            agentManagementAssistant.register(
                    createService(MasoesOntology.ACTION_EVALUATE_STIMULUS),
                    createService(MasoesOntology.ACTION_GET_EMOTIONAL_STATE)
            );
        } catch (Exception e) {
            logger.exception(this, e);
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

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("aid", getAID())
                .append("behaviouralComponent", behaviouralComponent)
                .toString();
    }

    public BehaviouralComponent getBehaviouralComponent() {
        return behaviouralComponent;
    }

    public abstract void setUp();

    public abstract String getKnowledgePath();

    public abstract ImitativeBehaviour getImitativeBehaviour();

    public abstract ReactiveBehaviour getReactiveBehaviour();

    public abstract CognitiveBehaviour getCognitiveBehaviour();

}
