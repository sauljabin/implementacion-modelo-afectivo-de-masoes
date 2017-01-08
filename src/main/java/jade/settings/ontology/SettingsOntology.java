/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.settings.ontology.elements.QueryAllSettings;
import jade.settings.ontology.elements.QuerySetting;
import jade.settings.ontology.elements.Setting;
import jade.settings.ontology.elements.SystemSettings;
import jade.settings.ontology.elements.UnexpectedContent;
import jade.settings.ontology.elements.UnexpectedPerformative;

import java.util.Optional;

public class SettingsOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "Settings";
    private static Ontology INSTANCE;

    private SettingsOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(Setting.class);
            add(QueryAllSettings.class);
            add(QuerySetting.class);
            add(SystemSettings.class);
            add(UnexpectedContent.class);
            add(UnexpectedPerformative.class);
        } catch (Exception e) {
            throw new SettingsOntologyException(e.getMessage(), e);
        }
    }

    public synchronized static Ontology getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new SettingsOntology();
        }
        return INSTANCE;
    }

}
