/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology;

import jade.content.onto.BeanOntology;
import jade.ontology.SetupOntologyException;
import jade.ontology.base.BaseOntology;

public class SettingsOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "settings";

    public SettingsOntology() {
        super(ONTOLOGY_NAME, new BaseOntology());
        try {
            add(SettingsOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e.getMessage(), e);
        }
    }

}
