/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.logger;

import common.logger.LogWriter;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import masoes.core.EmotionalAgent;
import masoes.core.Stimulus;
import org.slf4j.Logger;

import java.util.Optional;

public class JadeLogger {

    private static final String NO_BEHAVIOUR = "NO BEHAVIOUR";

    private Logger logger;

    public JadeLogger(Logger logger) {
        this.logger = logger;
    }

    public void agentException(Agent agent, Exception exception) {
        new LogWriter()
                .message("Exception in agent \"%s\": %s")
                .args(agent.getLocalName(), exception.getMessage())
                .exception(exception)
                .error(logger);
    }

    public void agentMessage(Agent agent, ACLMessage message) {
        new LogWriter()
                .message("Message in agent \"%s\": %s")
                .args(agent.getLocalName(), message)
                .info(logger);
    }

    public void agentEmotionalState(EmotionalAgent emotionalAgent) {
        LogWriter logWriter = new LogWriter()
                .message("Emotional state in agent \"%s\": emotion=%s, state=%s, behaviour=%s")
                .args(emotionalAgent.getLocalName(), emotionalAgent.getCurrentEmotion(), emotionalAgent.getEmotionalState());

        Optional<Behaviour> behaviourOptional = Optional.ofNullable(emotionalAgent.getEmotionalBehaviour());

        if (behaviourOptional.isPresent()) {
            logWriter.args(behaviourOptional.get().getBehaviourName());
        } else {
            logWriter.args(NO_BEHAVIOUR);
        }

        logWriter.info(logger);
    }

    public void agentEmotionalStateChanged(EmotionalAgent emotionalAgent, Stimulus stimulus) {
        LogWriter logWriter = new LogWriter()
                .message("Emotional state changed in agent \"%s\": stimulus=%s, emotion=%s, state=%s, behaviour=%s")
                .args(emotionalAgent.getLocalName(), stimulus, emotionalAgent.getCurrentEmotion(), emotionalAgent.getEmotionalState());

        Optional<Behaviour> behaviourOptional = Optional.ofNullable(emotionalAgent.getEmotionalBehaviour());

        if (behaviourOptional.isPresent()) {
            logWriter.args(behaviourOptional.get().getBehaviourName());
        } else {
            logWriter.args(NO_BEHAVIOUR);
        }

        logWriter.info(logger);
    }

}
