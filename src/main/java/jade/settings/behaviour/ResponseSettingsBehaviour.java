/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.behaviour;

import application.settings.ApplicationSettings;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.logger.JadeLogger;
import jade.ontology.base.UnexpectedContent;
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
        template = MessageTemplate.and(template, MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL));
        template = MessageTemplate.and(template, MessageTemplate.MatchOntology(SettingsOntology.ONTOLOGY_NAME));
        setMessageTemplate(template);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) {
        ACLMessage response = request.createReply();
        try {
            Action action = (Action) myAgent.getContentManager().extractContent(request);
            Concept agentAction = action.getAction();

            if (agentAction instanceof GetSetting) {
                getSettingResponse(response, (GetSetting) agentAction);
            } else if (agentAction instanceof GetAllSettings) {
                getSettingsResponse(response);
            } else {
                invalidActionResponse(request, response);
            }

        } catch (Exception e) {
            failureResponse(response, e);
        }
        return response;
    }

    private void invalidActionResponse(ACLMessage request, ACLMessage response) throws Exception {
        response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        UnexpectedContent unexpectedContent = new UnexpectedContent("Invalid agent action", request.getContent());
        myAgent.getContentManager().fillContent(response, unexpectedContent);
    }

    private void failureResponse(ACLMessage response, Exception e) {
        response.setPerformative(ACLMessage.FAILURE);
        response.setContent(e.getMessage());
        logger.agentException(myAgent, e);
    }

    private void getSettingsResponse(ACLMessage response) throws Exception {
        SystemSettings systemSettings = new SystemSettings(new ArrayList());

        applicationSettings.toMap().forEach(
                (key, value) -> systemSettings.getSettings().add(new Setting(key, value))
        );

        jadeSettings.toMap().forEach(
                (key, value) -> systemSettings.getSettings().add(new Setting(key, value))
        );

        response.setPerformative(ACLMessage.INFORM);
        myAgent.getContentManager().fillContent(response, systemSettings);
    }

    private void getSettingResponse(ACLMessage response, GetSetting getSetting) throws Exception {
        ContentElement contentElement;
        String setting = applicationSettings.get(getSetting.getKey());

        if (!Optional.ofNullable(setting).isPresent()) {
            setting = jadeSettings.get(getSetting.getKey());
        }

        if (Optional.ofNullable(setting).isPresent()) {
            response.setPerformative(ACLMessage.INFORM);
            SystemSettings systemSettings = new SystemSettings(new ArrayList());
            systemSettings.getSettings().add(new Setting(getSetting.getKey(), setting));
            contentElement = systemSettings;
        } else {
            response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            contentElement = new UnexpectedContent("Setting not found", getSetting.getKey());
        }

        myAgent.getContentManager().fillContent(response, contentElement);
    }

}
