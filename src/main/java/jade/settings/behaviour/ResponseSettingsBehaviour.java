/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.behaviour;

import application.settings.ApplicationSettings;
import jade.content.ContentManager;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.language.FipaLanguage;
import jade.protocol.ProtocolRequestResponderBehaviour;
import jade.settings.JadeSettings;
import jade.settings.ontology.GetAllSettings;
import jade.settings.ontology.GetSetting;
import jade.settings.ontology.Setting;
import jade.settings.ontology.SettingsOntology;
import jade.settings.ontology.SystemSettings;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import java.util.Map;

public class ResponseSettingsBehaviour extends ProtocolRequestResponderBehaviour {

    private MessageTemplate template;

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
        try {
            ContentManager contentManager = myAgent.getContentManager();
            Action action = (Action) contentManager.extractContent(request);

            if (action.getAction() instanceof GetSetting) {
                GetSetting getSetting = (GetSetting) action.getAction();
                String setting = ApplicationSettings.getInstance().get(getSetting.getKey());

                ACLMessage reply = request.createReply();

                SystemSettings systemSettings = new SystemSettings();
                ArrayList settings = new ArrayList();
                settings.add(new Setting(getSetting.getKey(), setting));
                systemSettings.setSettings(settings);
                contentManager.fillContent(reply, systemSettings);

                return reply;
            } else if (action.getAction() instanceof GetAllSettings) {
                GetAllSettings getAllSettings = (GetAllSettings) action.getAction();

                ACLMessage reply = request.createReply();

                SystemSettings systemSettings = new SystemSettings();
                List appList = new ArrayList();

                Map<String, String> appSettings = ApplicationSettings.getInstance().toMap();
                appSettings.forEach((key, value) -> appList.add(new Setting(key, value)));

                Map<String, String> jadeSettings = JadeSettings.getInstance().toMap();
                jadeSettings.forEach((key, value) -> appList.add(new Setting(key, value)));

                systemSettings.setSettings(appList);

                contentManager.fillContent(reply, systemSettings);

                return reply;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return request;
    }

}
