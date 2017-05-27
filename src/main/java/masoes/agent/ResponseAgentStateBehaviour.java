/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import ontology.ActionResponderBehaviour;

public class ResponseAgentStateBehaviour extends ActionResponderBehaviour {

    private EmotionalAgent emotionalAgent;
    private BehaviouralComponent behaviouralComponent;

    public ResponseAgentStateBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent, MasoesOntology.getInstance(), GetEmotionalState.class);
        this.emotionalAgent = emotionalAgent;
        this.behaviouralComponent = emotionalAgent.getBehaviouralComponent();
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        return new AgentState(emotionalAgent.getAID(), behaviouralComponent);
    }

}
