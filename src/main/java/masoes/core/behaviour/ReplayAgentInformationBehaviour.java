/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.app.logger.ApplicationLogger;
import masoes.core.EmotionalAgent;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReplayAgentInformationBehaviour extends Behaviour {

    private static final String AGENT_KEY = "agent";
    private static final String EMOTION_KEY = "emotion";
    private static final String BEHAVIOUR_KEY = "behaviour";
    private static final String STATE_KEY = "state";
    private MessageTemplate template;
    private EmotionalAgent emotionalAgent;
    private ApplicationLogger logger;

    public ReplayAgentInformationBehaviour(EmotionalAgent emotionalAgent, ApplicationLogger logger) {
        super(emotionalAgent);
        this.logger = logger;
        this.emotionalAgent = emotionalAgent;
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    public ReplayAgentInformationBehaviour(EmotionalAgent emotionalAgent) {
        this(emotionalAgent, new ApplicationLogger(LoggerFactory.getLogger(ReplayAgentInformationBehaviour.class)));
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);
        if (Optional.ofNullable(msg).isPresent()) {
            logger.agentMessage(myAgent, msg);
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent(createContent());
            myAgent.send(reply);
        } else {
            block();
        }
    }

    private String createContent() {
        Map<String, Object> content = new HashMap<>();
        content.put(AGENT_KEY, emotionalAgent.getName());
        content.put(EMOTION_KEY, emotionalAgent.getCurrentEmotion().getName());
        content.put(BEHAVIOUR_KEY, emotionalAgent.getEmotionalBehaviour().getBehaviourName());
        content.put(STATE_KEY, emotionalAgent.getEmotionalState());
        return content.toString();
    }

    @Override
    public boolean done() {
        return false;
    }

}
