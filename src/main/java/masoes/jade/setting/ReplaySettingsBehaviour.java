/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.app.logger.ApplicationLogger;
import masoes.app.setting.Setting;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ReplaySettingsBehaviour extends CyclicBehaviour {

    private MessageTemplate template;
    private ApplicationLogger logger;

    public ReplaySettingsBehaviour() {
        this(null);
    }

    public ReplaySettingsBehaviour(Agent agent) {
        super(agent);
        logger = new ApplicationLogger(LoggerFactory.getLogger(ReplaySettingsBehaviour.class));
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);

        if (isPresent(msg)) {
            logger.agentMessage(myAgent, msg);

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            if (isNullContent(msg)) {
                reply.setContent(getContentAllSettings());
            } else if (isSetting(msg)) {
                reply.setContent(Setting.get(msg.getContent()));
            } else {
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            }

            myAgent.send(reply);

        } else {
            block();
        }

    }

    private String getContentAllSettings() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(Setting.toMap());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private boolean isPresent(ACLMessage msg) {
        return Optional.ofNullable(msg).isPresent();
    }

    private boolean isSetting(ACLMessage msg) {
        return Optional.ofNullable(Setting.get(msg.getContent(), null)).isPresent();
    }

    private boolean isNullContent(ACLMessage msg) {
        return !Optional.ofNullable(msg.getContent()).isPresent();
    }

}
