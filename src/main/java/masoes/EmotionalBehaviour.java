/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import environment.Environment;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.behavioural.BehaviourType;
import ontology.OntologyAssistant;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.MasoesOntology;
import masoes.ontology.notifier.NotifyAction;

public abstract class EmotionalBehaviour extends Behaviour {

    public abstract String getName();

    public abstract BehaviourType getType();

    public void notifyAction(String actionName) {
        OntologyAssistant ontologyAssistant = new OntologyAssistant(myAgent, MasoesOntology.getInstance());
        ActionStimulus actionStimulus = new ActionStimulus(myAgent.getAID(), actionName);
        NotifyAction notifyAction = new NotifyAction(actionStimulus);
        ACLMessage requestAction = ontologyAssistant.createRequestAction(myAgent.getAID(Environment.NOTIFIER_AGENT), notifyAction);
        myAgent.send(requestAction);
    }

}
