/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behaviour;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.logger.JadeLogger;
import jade.ontology.base.ActionResult;
import masoes.core.EmotionalAgent;
import masoes.ontology.EvaluateStimulus;
import org.slf4j.LoggerFactory;

public class StimulusReceiverBehaviour extends MasoesResponderBehaviour {

    private EmotionalAgent emotionalAgent;
    private JadeLogger logger;

    public StimulusReceiverBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent);
        this.emotionalAgent = emotionalAgent;
        logger = new JadeLogger(LoggerFactory.getLogger(StimulusReceiverBehaviour.class));
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
