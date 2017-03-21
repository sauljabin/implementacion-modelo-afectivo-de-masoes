/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.component.cognitive.CognitiveComponent;
import masoes.component.reactive.ReactiveComponent;
import masoes.component.social.SocialComponent;
import masoes.ontology.MasoesOntology;
import util.ServiceBuilder;
import util.ToStringBuilder;

public abstract class EmotionalAgent extends Agent {

    private BehaviouralComponent behaviouralComponent;
    private EmotionalAgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;
    private CognitiveComponent cognitiveComponent;
    private ReactiveComponent reactiveComponent;
    private SocialComponent socialComponent;

    public EmotionalAgent() {
        logger = new EmotionalAgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected final void setup() {
        try {
            behaviouralComponent = new BehaviouralComponent(this);
            cognitiveComponent = new CognitiveComponent(this);
            reactiveComponent = new ReactiveComponent(this);
            socialComponent = new SocialComponent(this);

            addBehaviour(new EmotionalAgentBehaviour(this));

            setUp();

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

    public CognitiveComponent getCognitiveComponent() {
        return cognitiveComponent;
    }

    public ReactiveComponent getReactiveComponent() {
        return reactiveComponent;
    }

    public SocialComponent getSocialComponent() {
        return socialComponent;
    }

    public abstract void setUp();

}
