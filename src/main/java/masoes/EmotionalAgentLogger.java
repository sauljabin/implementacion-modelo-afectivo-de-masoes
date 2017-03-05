/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.lang.acl.ACLMessage;
import logger.LogWriter;
import masoes.behavioural.BehaviourType;
import masoes.behavioural.Emotion;
import ontology.masoes.Stimulus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmotionalAgentLogger {

    private EmotionalAgent emotionalAgent;
    private Logger logger;

    public EmotionalAgentLogger(EmotionalAgent emotionalAgent) {
        this.emotionalAgent = emotionalAgent;
        logger = LoggerFactory.getLogger(emotionalAgent.getClass());
    }

    public Logger getLogger() {
        return logger;
    }

    public void exception(Exception exception) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Exception in agent \"%s\", class \"%s\": %s")
                .args(emotionalAgent.getLocalName(), emotionalAgent.getClass().getName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void messageRequest(ACLMessage message) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Message request in agent \"%s\": %s")
                .args(emotionalAgent.getLocalName(), message)
                .info(logger);
    }

    public void messageResponse(ACLMessage message) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Message response in agent \"%s\": %s")
                .args(emotionalAgent.getLocalName(), message)
                .info(logger);
    }

    public void agentInfo() {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Agent %s: %s")
                .args(emotionalAgent.getLocalName(), emotionalAgent)
                .info(logger);
    }

    public void info(String info) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Agent %s: %s")
                .args(emotionalAgent.getLocalName(), info)
                .info(logger);
    }

    public void updatingBehaviour(BehaviourType actualType, BehaviourType newType) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Actualizando comportamiento %s a %s")
                .args(actualType, newType)
                .info(logger);
    }

    public void startingBehaviour(BehaviourType newType) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Inicializando comportamiento %s")
                .args(newType)
                .info(logger);
    }

    public void updatingEmotion(Emotion actualEmotion, Emotion newEmotion) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Actualizando emoción %s a %s")
                .args(actualEmotion.getName().toUpperCase(), newEmotion.getName().toUpperCase())
                .info(logger);
    }

    public void receivingStimulus(Stimulus stimulus) {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Recibiendo mensaje: %s")
                .args(stimulus)
                .info(logger);
    }

    public void sendingDone() {
        new LogWriter()
                .addLoggerHandler(emotionalAgent)
                .message("Enviando respuesta: completado")
                .info(logger);
    }

}
