/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.app.logger.ApplicationLogger;
import masoes.app.settings.ApplicationSettings;
import masoes.jade.settings.JadeSettings;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReplaySettingsBehaviour extends CyclicBehaviour {

    private static final String APPLICATION_SETTINGS = "applicationSettings";
    private static final String JADE_SETTINGS = "jadeSettings";
    private MessageTemplate template;
    private ApplicationLogger logger;
    private JadeSettings jadeSettings;
    private ApplicationSettings applicationSettings;

    public ReplaySettingsBehaviour() {
        this(null);
    }

    public ReplaySettingsBehaviour(Agent agent) {
        super(agent);
        logger = new ApplicationLogger(LoggerFactory.getLogger(ReplaySettingsBehaviour.class));
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        jadeSettings = JadeSettings.getInstance();
        applicationSettings = ApplicationSettings.getInstance();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);

        if (isPresent(msg)) {
            logger.agentMessage(myAgent, msg);

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            try {
                if (isNullContent(msg)) {
                    reply.setContent(getContentAllSettings());
                } else if (isSetting(msg.getContent())) {
                    reply.setContent(getContent(msg.getContent()));
                } else {
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                }
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

    private String getContent(String key) {
        return Optional.ofNullable(applicationSettings.get(key)).orElse(jadeSettings.get(key));
    }

    private String getContentAllSettings() throws Exception {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put(APPLICATION_SETTINGS, applicationSettings.toMap());
        objectMap.put(JADE_SETTINGS, jadeSettings.toMap());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(objectMap);
    }

    private boolean isPresent(ACLMessage msg) {
        return Optional.ofNullable(msg).isPresent();
    }

    private boolean isSetting(String key) {
        return Optional.ofNullable(applicationSettings.get(key)).isPresent() || Optional.ofNullable(jadeSettings.get(key)).isPresent();
    }

    private boolean isNullContent(ACLMessage msg) {
        return !Optional.ofNullable(msg.getContent()).isPresent();
    }

}
