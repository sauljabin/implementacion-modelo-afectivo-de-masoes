/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import application.logger.ApplicationLogger;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.core.EmotionalAgent;
import masoes.core.Stimulus;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class StimulusReceiverBehaviour extends Behaviour {

    private MessageTemplate template;
    private EmotionalAgent emotionalAgent;
    private ApplicationLogger logger;

    public StimulusReceiverBehaviour(EmotionalAgent emotionalAgent) {
        super(emotionalAgent);
        this.emotionalAgent = emotionalAgent;
        logger = new ApplicationLogger(LoggerFactory.getLogger(StimulusReceiverBehaviour.class));
        template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);
        if (Optional.ofNullable(msg).isPresent()) {
            logger.agentMessage(myAgent, msg);
            emotionalAgent.evaluateStimulus(new Stimulus());
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
