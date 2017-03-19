/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import application.ApplicationSettings;
import jade.JadeSettings;
import jade.content.Concept;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import masoes.MasoesSettings;
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import ontology.settings.GetAllSettings;
import ontology.settings.GetSetting;
import ontology.settings.Setting;
import ontology.settings.SettingsOntology;
import ontology.settings.SystemSettings;

import java.util.Arrays;

public class ResponseSettingsBehaviour extends OntologyResponderBehaviour {

    private ApplicationSettings applicationSettings;
    private JadeSettings jadeSettings;
    private MasoesSettings masoesSettings;

    public ResponseSettingsBehaviour(Agent agent) {
        super(agent, new MessageTemplate(new OntologyMatchExpression(SettingsOntology.getInstance())), SettingsOntology.getInstance());
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
        masoesSettings = MasoesSettings.getInstance();
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetSetting.class, GetAllSettings.class)
                .contains(action.getAction().getClass());
    }

    @Override
    public Predicate performAction(Action action) {
        Concept agentAction = action.getAction();
        if (agentAction instanceof GetSetting) {
            return getSettingResponse((GetSetting) agentAction);
        } else {
            return getSettingsResponse();
        }
    }

    private Predicate getSettingsResponse() {
        SystemSettings systemSettings = new SystemSettings(new ArrayList());

        applicationSettings.toMap().forEach(
                (key, value) -> systemSettings.getSettings().add(new Setting(key, value))
        );

        jadeSettings.toMap().forEach(
                (key, value) -> systemSettings.getSettings().add(new Setting(key, value))
        );

        masoesSettings.toMap().forEach(
                (key, value) -> systemSettings.getSettings().add(new Setting(key, value))
        );

        return systemSettings;
    }

    private Predicate getSettingResponse(GetSetting getSetting) {
        Predicate contentElement;
        String setting = applicationSettings.get(getSetting.getKey());

        if (setting == null) {
            setting = jadeSettings.get(getSetting.getKey());
        }

        if (setting == null) {
            setting = masoesSettings.get(getSetting.getKey());
        }

        if (setting != null) {
            SystemSettings systemSettings = new SystemSettings(new ArrayList());
            systemSettings.getSettings().add(new Setting(getSetting.getKey(), setting));
            contentElement = systemSettings;
        } else {
            throw new SettingsException(String.format("Setting not found %s", getSetting.getKey()));
        }

        return contentElement;
    }

}
