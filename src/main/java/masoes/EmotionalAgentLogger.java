/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.lang.acl.ACLMessage;
import logger.LogWriter;
import masoes.behavioural.BehaviourType;
import masoes.behavioural.Emotion;
import ontology.masoes.stimulus.Stimulus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import translate.Translation;

public class EmotionalAgentLogger {

    private EmotionalAgent emotionalAgent;
    private Logger logger;
    private Translation translation;

    public EmotionalAgentLogger(EmotionalAgent emotionalAgent) {
        this.emotionalAgent = emotionalAgent;
        logger = LoggerFactory.getLogger(emotionalAgent.getClass());
        translation = Translation.getInstance();
    }

    public void exception(Exception exception) {
        new LogWriter()
                .message(translation.get("log.exception_in_agent"))
                .args(emotionalAgent.getLocalName(), emotionalAgent.getClass().getName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void messageRequest(ACLMessage message) {
        new LogWriter()
                .message(translation.get("log.request_message"))
                .args(emotionalAgent.getLocalName(), message)
                .info(logger);
    }

    public void messageResponse(ACLMessage message) {
        new LogWriter()
                .message(translation.get("log.response_message"))
                .args(emotionalAgent.getLocalName(), message)
                .info(logger);
    }

    public void agentInfo() {
        new LogWriter()
                .message(translation.get("log.agent"))
                .args(emotionalAgent.getLocalName(), emotionalAgent)
                .info(logger);
    }

    public void info(String info) {
        new LogWriter()
                .message(translation.get("log.agent"))
                .args(emotionalAgent.getLocalName(), info)
                .info(logger);
    }

    public void updatingBehaviour(BehaviourType actualType, BehaviourType newType) {
        new LogWriter()
                .message(translation.get("log.updating_behaviour"))
                .args(translation.get(actualType.toString().toLowerCase()), translation.get(newType.toString().toLowerCase()))
                .info(logger);
    }

    public void startingBehaviour(BehaviourType newType) {
        new LogWriter()
                .message(translation.get("log.starting_behaviour"))
                .args(translation.get(newType.toString().toLowerCase()))
                .info(logger);
    }

    public void updatingEmotion(Emotion actualEmotion, Emotion newEmotion) {
        new LogWriter()
                .message(translation.get("log.updating_emotion"))
                .args(translation.get(actualEmotion.getName().toLowerCase()), translation.get(newEmotion.getName().toLowerCase()))
                .info(logger);
    }

    public void receivingStimulus(Stimulus stimulus) {
        new LogWriter()
                .message(translation.get("log.receiving_message"))
                .args(stimulus)
                .info(logger);
    }

    public void sendingDone() {
        new LogWriter()
                .message(translation.get("log.sending_done"))
                .info(logger);
    }

}
