/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent.configurable;

import agent.configurable.ontology.AddBehaviour;
import agent.configurable.ontology.ConfigurableOntology;
import agent.configurable.ontology.RemoveBehaviour;
import jade.content.Concept;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import ontology.OntologyResponderBehaviour;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigurableAgentBehaviour extends OntologyResponderBehaviour {

    private Map<String, Behaviour> behaviours;

    public ConfigurableAgentBehaviour(Agent agent) {
        super(agent, ConfigurableOntology.getInstance());
        behaviours = new HashMap<>();
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(
                RemoveBehaviour.class,
                AddBehaviour.class
        ).contains(action.getAction().getClass());
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        Concept agentAction = action.getAction();
        if (agentAction instanceof AddBehaviour) {
            return addBehaviour(action);
        } else if (agentAction instanceof RemoveBehaviour) {
            return removeBehaviour(action);
        } else {
            throw new FailureException("Unknown action " + agentAction);
        }
    }

    private Predicate removeBehaviour(Action action) {
        RemoveBehaviour removeBehaviour = (RemoveBehaviour) action.getAction();
        String name = removeBehaviour.getName();
        if (behaviours.get(name) != null) {
            myAgent.removeBehaviour(behaviours.get(name));
        }
        return new Done(action);
    }

    private Predicate addBehaviour(Action action) throws FailureException {
        AddBehaviour addBehaviour = (AddBehaviour) action.getAction();
        String name = addBehaviour.getName();
        if (!Optional.ofNullable(name).orElse("").isEmpty()) {
            try {
                Behaviour behaviour = (Behaviour) Class.forName(addBehaviour.getClassName()).newInstance();
                behaviours.put(name, behaviour);
                myAgent.addBehaviour(behaviour);
                return new Done(action);
            } catch (Exception e) {
                throw new FailureException(e.getMessage());
            }
        } else {
            throw new FailureException("Behaviour name not found");
        }
    }

}
