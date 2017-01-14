/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.ontology.SetupOntologyException;

import java.util.Optional;

public class SettingsOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "settings";
    private static Ontology INSTANCE;

    private SettingsOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(SettingsOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e.getMessage(), e);
        }
    }

    public synchronized static Ontology getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new SettingsOntology();
        }
        return INSTANCE;
    }

}
