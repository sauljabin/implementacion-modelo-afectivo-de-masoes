/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import knowledge.Knowledge;
import masoes.behavioural.BehaviouralComponent;
import ontology.masoes.MasoesOntology;
import util.ServiceBuilder;
import util.ToStringBuilder;

public abstract class EmotionalAgent extends Agent {

    private BehaviouralComponent behaviouralComponent;
    private EmotionalAgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public EmotionalAgent() {
        logger = new EmotionalAgentLogger(this);
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
            logger.exception(e);
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

    public abstract Knowledge getKnowledge();

    public abstract ImitativeBehaviour getImitativeBehaviour();

    public abstract ReactiveBehaviour getReactiveBehaviour();

    public abstract CognitiveBehaviour getCognitiveBehaviour();

    public EmotionalAgentLogger getLogger() {
        return logger;
    }

}
