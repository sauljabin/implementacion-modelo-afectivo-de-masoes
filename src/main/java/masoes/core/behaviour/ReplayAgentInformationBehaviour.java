/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.app.logger.ApplicationLogger;
import masoes.core.Emotion;
import masoes.core.EmotionalAgent;
import masoes.core.EmotionalState;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReplayAgentInformationBehaviour extends Behaviour {

    private static final String AGENT_KEY = "agent";
    private static final String EMOTION_KEY = "emotion";
    private static final String BEHAVIOUR_KEY = "behaviour";
    private static final String STATE_KEY = "state";
    private static final String NO_BEHAVIOUR = "NO BEHAVIOUR";
    private static final String NO_EMOTION = "NO EMOTION";
    private static final String NO_STATE = "NO STATE";
    private MessageTemplate template;
    private EmotionalAgent emotionalAgent;
    private ApplicationLogger logger;

    public ReplayAgentInformationBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent);
        this.emotionalAgent = emotionalAgent;
        logger = new ApplicationLogger(LoggerFactory.getLogger(ReplayAgentInformationBehaviour.class));
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);
        if (Optional.ofNullable(msg).isPresent()) {
            logger.agentMessage(myAgent, msg);
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContent(createContent());
            } catch (Exception e) {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent(e.getMessage());
                logger.agentException(myAgent, e);
            }
            myAgent.send(reply);
        } else {
            block();
        }
    }

    private String createContent() throws Exception {
        Map<String, Object> content = new HashMap<>();
        content.put(AGENT_KEY, emotionalAgent.getName());

        Optional<Emotion> emotionOptional = Optional.ofNullable(emotionalAgent.getCurrentEmotion());
        if (emotionOptional.isPresent()) {
            content.put(EMOTION_KEY, emotionOptional.get().getName());
        } else {
            content.put(EMOTION_KEY, NO_EMOTION);
        }

        Optional<Behaviour> behaviourOptional = Optional.ofNullable(emotionalAgent.getEmotionalBehaviour());
        if (behaviourOptional.isPresent()) {
            content.put(BEHAVIOUR_KEY, behaviourOptional.get().getBehaviourName());
        } else {
            content.put(BEHAVIOUR_KEY, NO_BEHAVIOUR);
        }

        Optional<EmotionalState> stateOptional = Optional.ofNullable(emotionalAgent.getEmotionalState());
        if (stateOptional.isPresent()) {
            content.put(STATE_KEY, stateOptional.get());
        } else {
            content.put(STATE_KEY, NO_STATE);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(content);
    }

    @Override
    public boolean done() {
        return false;
    }

}
