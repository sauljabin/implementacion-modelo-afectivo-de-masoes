/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behaviour;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.ontology.base.ActionResult;
import jade.protocol.OntologyResponderBehaviour;
import masoes.message.MasoesRequestMessageTemplate;
import masoes.model.EmotionalAgent;
import masoes.ontology.EvaluateStimulus;
import masoes.ontology.MasoesOntology;

public class StimulusReceiverBehaviour extends OntologyResponderBehaviour {

    private EmotionalAgent emotionalAgent;

    public StimulusReceiverBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent, new MasoesRequestMessageTemplate(), new MasoesOntology());
        this.emotionalAgent = emotionalAgent;
    }

    @Override
    public boolean isValidAction(Action action) {
        return super.isValidAction(action);
    }

    @Override
    public Predicate performAction(Action action) {
        EvaluateStimulus evaluateStimulus = (EvaluateStimulus) action.getAction();
        emotionalAgent.evaluateStimulus(evaluateStimulus.getStimulus());
        return new ActionResult("Ok", evaluateStimulus);
    }

}
