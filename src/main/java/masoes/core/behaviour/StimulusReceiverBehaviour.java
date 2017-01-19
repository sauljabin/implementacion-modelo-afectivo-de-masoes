/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.logger.JadeLogger;
import jade.ontology.base.ActionResult;
import jade.ontology.base.UnexpectedContent;
import masoes.core.EmotionalAgent;
import masoes.core.ontology.EvaluateStimulus;
import org.slf4j.LoggerFactory;

public class StimulusReceiverBehaviour extends MasoesResponderBehaviour {

    private MessageTemplate template;
    private EmotionalAgent emotionalAgent;
    private JadeLogger logger;

    public StimulusReceiverBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent);
        this.emotionalAgent = emotionalAgent;
        logger = new JadeLogger(LoggerFactory.getLogger(StimulusReceiverBehaviour.class));
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) {
        ACLMessage response = request.createReply();
        try {
            Action action = (Action) myAgent.getContentManager().extractContent(request);
            Concept agentAction = action.getAction();

            if (agentAction instanceof EvaluateStimulus) {
                evaluateStimulusResponse(response, (EvaluateStimulus) agentAction);
            } else {
                invalidActionResponse(request, response);
            }

        } catch (Exception e) {
            failureResponse(response, e);
        }
        return response;
    }

    private void evaluateStimulusResponse(ACLMessage response, EvaluateStimulus agentAction) throws Codec.CodecException, OntologyException {
        response.setPerformative(ACLMessage.INFORM);
        EvaluateStimulus evaluateStimulus = agentAction;
        emotionalAgent.evaluateStimulus(evaluateStimulus.getStimulus());
        ActionResult actionResult = new ActionResult("Ok", agentAction);
        emotionalAgent.getContentManager().fillContent(response, actionResult);
    }

    private void invalidActionResponse(ACLMessage request, ACLMessage response) throws Exception {
        response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        UnexpectedContent unexpectedContent = new UnexpectedContent("Invalid agent action", request.getContent());
        myAgent.getContentManager().fillContent(response, unexpectedContent);
    }

    private void failureResponse(ACLMessage response, Exception e) {
        response.setPerformative(ACLMessage.FAILURE);
        response.setContent(e.getMessage());
        logger.agentException(myAgent, e);
    }


}
