/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.core.EmotionalAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReplayAgentInformationBehaviour extends Behaviour {

    private MessageTemplate template;
    private EmotionalAgent emotionalAgent;

    public ReplayAgentInformationBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent);
        this.emotionalAgent = emotionalAgent;
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);

        if (Optional.ofNullable(msg).isPresent()) {

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
        content.put("emotion", emotionalAgent.getCurrentEmotion());
        content.put("behaviour", emotionalAgent.getEmotionalBehaviour());
        return content.toString();
    }

    @Override
    public boolean done() {
        return false;
    }

}
