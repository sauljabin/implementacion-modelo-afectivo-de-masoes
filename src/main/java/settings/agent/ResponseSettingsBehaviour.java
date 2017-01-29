/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings.agent;

import jade.content.Concept;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.protocol.OntologyResponderBehaviour;
import jade.util.leap.ArrayList;
import settings.loader.ApplicationSettings;
import settings.exception.SettingsException;
import settings.loader.JadeSettings;
import settings.ontology.GetAllSettings;
import settings.ontology.GetSetting;
import settings.ontology.Setting;
import settings.ontology.SettingsOntology;
import settings.ontology.SystemSettings;

import java.util.Arrays;
import java.util.Optional;

public class ResponseSettingsBehaviour extends OntologyResponderBehaviour {

    private final ApplicationSettings applicationSettings;
    private final JadeSettings jadeSettings;

    public ResponseSettingsBehaviour(Agent agent) {
        super(agent, new SettingsRequestMessageTemplate(), new SettingsOntology());
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
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

        return systemSettings;
    }

    private Predicate getSettingResponse(GetSetting getSetting) {
        Predicate contentElement;
        String setting = applicationSettings.get(getSetting.getKey());

        if (!Optional.ofNullable(setting).isPresent()) {
            setting = jadeSettings.get(getSetting.getKey());
        }

        if (Optional.ofNullable(setting).isPresent()) {
            SystemSettings systemSettings = new SystemSettings(new ArrayList());
            systemSettings.getSettings().add(new Setting(getSetting.getKey(), setting));
            contentElement = systemSettings;
        } else {
            throw new SettingsException(String.format("Setting not found %s", getSetting.getKey()));
        }

        return contentElement;
    }

}
