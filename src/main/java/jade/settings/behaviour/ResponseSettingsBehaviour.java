/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.behaviour;

import application.settings.ApplicationSettings;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.language.FipaLanguage;
import jade.logger.JadeLogger;
import jade.protocol.ProtocolRequestResponderBehaviour;
import jade.settings.JadeSettings;
import jade.settings.ontology.GetAllSettings;
import jade.settings.ontology.GetSetting;
import jade.settings.ontology.Setting;
import jade.settings.ontology.SettingsOntology;
import jade.settings.ontology.SystemSettings;
import jade.util.leap.ArrayList;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ResponseSettingsBehaviour extends ProtocolRequestResponderBehaviour {

    private final ApplicationSettings applicationSettings;
    private final JadeSettings jadeSettings;
    private JadeLogger logger;
    private MessageTemplate template;

    public ResponseSettingsBehaviour() {
        logger = new JadeLogger(LoggerFactory.getLogger(ResponseSettingsBehaviour.class));
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
    }

    @Override
    public void onStart() {
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        template = MessageTemplate.and(template, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        template = MessageTemplate.and(template, MessageTemplate.MatchLanguage(FipaLanguage.LANGUAGE_NAME));
        template = MessageTemplate.and(template, MessageTemplate.MatchOntology(SettingsOntology.ONTOLOGY_NAME));
        setMessageTemplate(template);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) {
        ContentManager contentManager = myAgent.getContentManager();
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            Action action = (Action) contentManager.extractContent(request);
            Concept agentAction = action.getAction();
            ContentElement contentElement = null;

            if (agentAction instanceof GetSetting) {
                contentElement = getSetting((GetSetting) agentAction);
            } else if (agentAction instanceof GetAllSettings) {
                contentElement = getSettings();
            }

            contentManager.fillContent(reply, contentElement);
        } catch (Exception e) {
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent(e.getMessage());
            logger.agentException(myAgent, e);
        }
        return reply;
    }

    private ContentElement getSettings() {
        SystemSettings systemSettings = new SystemSettings(new ArrayList());

        applicationSettings.toMap().forEach(
                (key, value) -> systemSettings.getSettings().add(new Setting(key, value))
        );

        jadeSettings.toMap().forEach(
                (key, value) -> systemSettings.getSettings().add(new Setting(key, value))
        );

        return systemSettings;
    }

    private ContentElement getSetting(GetSetting getSetting) {

        String setting = applicationSettings.get(getSetting.getKey());

        if (!Optional.ofNullable(setting).isPresent()) {
            setting = jadeSettings.get(getSetting.getKey());
        }

        SystemSettings systemSettings = new SystemSettings(new ArrayList());
        systemSettings.getSettings().add(new Setting(getSetting.getKey(), setting));
        return systemSettings;
    }

}
